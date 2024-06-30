package com.example.interceptorhandler.service.impl;

import com.example.interceptorhandler.entities.LockedIp;
import com.example.interceptorhandler.repository.LockedIpRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LockedIpHandler {
    private final LockedIpRepository lockedIpRepository;

    public void handleLockedIpAfterCompletion(HttpServletRequest request, HttpServletResponse response, String body) {
        log.info("Request from IP: {} to endpoint: {}", request.getRemoteAddr(), request.getRequestURI());
        log.info("LockedIpHandler :: Body: {}", body);
        if (response.getStatus() != 200) {
            LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(1);
            String requestBody = StringUtils.isEmpty(body) ? "" : body;
            List<LockedIp> lockedIps = lockedIpRepository.findByIpaddressAndEndpointAndMethodAndRequestBody(
                    request.getRemoteAddr(),
                    request.getRequestURI(),
                    request.getMethod(),
                    requestBody
            );
            List<LockedIp> thresholdLockedIps = lockedIps.stream()
                    .filter(lockedIp -> lockedIp.getLockedUntil().isAfter(timeThreshold))
                    .toList();
            if (!CollectionUtils.isEmpty(thresholdLockedIps)) {
                updateExistingLockedIp(lockedIps.get(0));
            } else {
                createNewLockedIp(request, requestBody);
            }
        }
    }

    private void updateExistingLockedIp(LockedIp lockedIp) {
        lockedIp.setNumberOfRequests(lockedIp.getNumberOfRequests() + 1);
        lockedIpRepository.save(lockedIp);
    }

    private void createNewLockedIp(HttpServletRequest request, String requestBody) {
        LockedIp lockedIp = LockedIp.builder()
                .ipAddress(request.getRemoteAddr())
                .endpoint(request.getRequestURI())
                .method(request.getMethod())
                .requestBody(requestBody)
                .lockedUntil(LocalDateTime.now())
                .numberOfRequests(1)
                .build();
        lockedIpRepository.save(lockedIp);
    }

    public boolean isLocked(String ipAddress, String endpoint, String method, String body) {
        log.info("isLocked:: IP: {} Endpoint: {} Method: {} Body: {}", ipAddress, endpoint, method, body);
        List<LockedIp> lockedIps = lockedIpRepository.findByIpaddressAndEndpointAndMethodAndRequestBody(ipAddress, endpoint, method, body);
        return isRequestLimitReached(filterByTimeThreshold(lockedIps));
    }

    private List<LockedIp> filterByTimeThreshold(List<LockedIp> lockedIps) {
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(1);
        return lockedIps.stream()
                .filter(lockedIp -> lockedIp.getLockedUntil().isAfter(timeThreshold))
                .collect(Collectors.toList());
    }

    private boolean isRequestLimitReached(List<LockedIp> lockedIps) {
        return !lockedIps.isEmpty() && lockedIps.get(0).getNumberOfRequests() >= 5;
    }
}

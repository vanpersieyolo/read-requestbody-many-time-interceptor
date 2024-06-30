package com.example.interceptorhandler.configuration;

import com.example.interceptorhandler.configuration.cache.CachedBodyHttpServletRequest;
import com.example.interceptorhandler.exception.LockedIpException;
import com.example.interceptorhandler.service.impl.LockedIpHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiRequestInterceptor implements HandlerInterceptor {
    public static final String CACHED_BODY_HTTP_SERVLET_REQUEST = "cachedBodyHttpServletRequest";

    @Autowired
    private LockedIpHandler lockedIpHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        request.setAttribute(CACHED_BODY_HTTP_SERVLET_REQUEST, cachedRequest);

        String ipAddress = request.getRemoteAddr();
        String endpoint = request.getRequestURI();
        String requestBody = new String(cachedRequest.getCachedBody(), StandardCharsets.UTF_8);

        log.info("Request from IP: {} to endpoint: {}", ipAddress, endpoint);
        log.info("RequestBody: {}", requestBody);

        boolean isLocked = lockedIpHandler.isLocked(ipAddress, endpoint, request.getMethod(), requestBody);
        if (isLocked) {
            throw new LockedIpException("Too many requests from this IP. Please try again later.");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // Retrieve the cached request from the request attributes
        CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request.getAttribute(CACHED_BODY_HTTP_SERVLET_REQUEST);
        String requestBody = new String(cachedRequest.getCachedBody(), StandardCharsets.UTF_8);

        log.info("AfterCompletion :: --> RequestBody: {}", requestBody);

        lockedIpHandler.handleLockedIpAfterCompletion(request, response, requestBody);

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
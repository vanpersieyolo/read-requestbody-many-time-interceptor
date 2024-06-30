package com.example.interceptorhandler.service.impl;

import com.example.interceptorhandler.entities.LockedIp;
import com.example.interceptorhandler.repository.LockedIpRepository;
import com.example.interceptorhandler.service.ILockedIpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LockedIpServiceImpl implements ILockedIpService {
    private final LockedIpRepository lockedIpRepository;


    @Override
    public boolean isLocked(String ipaddress, String endpoint, String method, String requestBody) {
        return false;
    }
}

package com.example.interceptorhandler.service;

public interface ILockedIpService {
    boolean isLocked(String ipaddress, String endpoint, String method, String requestBody);
}

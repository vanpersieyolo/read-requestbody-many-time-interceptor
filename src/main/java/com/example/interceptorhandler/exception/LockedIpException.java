package com.example.interceptorhandler.exception;

public class LockedIpException extends RuntimeException{
    public LockedIpException(String message) {
        super(message);
    }
}

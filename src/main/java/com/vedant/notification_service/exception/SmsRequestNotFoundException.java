package com.vedant.notification_service.exception;

import org.springframework.http.HttpStatus;

public class SmsRequestNotFoundException extends RuntimeException{
    private final HttpStatus status = HttpStatus.NOT_FOUND;
    private String message;
    public SmsRequestNotFoundException(String message){
        super(message);
        this.message = message;
    }
}

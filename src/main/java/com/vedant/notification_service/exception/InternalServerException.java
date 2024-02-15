package com.vedant.notification_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends RuntimeException{
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private String message;
    public InternalServerException(String message){
        super(message);
        this.message = message;
    }

}

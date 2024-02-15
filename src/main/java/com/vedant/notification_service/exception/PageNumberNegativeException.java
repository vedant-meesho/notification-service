package com.vedant.notification_service.exception;

import org.springframework.http.HttpStatus;

public class PageNumberNegativeException extends BadRequestException{
    public PageNumberNegativeException(){
        super("Page number cannot be negative!");
    }
}

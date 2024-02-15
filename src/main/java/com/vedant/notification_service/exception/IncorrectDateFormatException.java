package com.vedant.notification_service.exception;

import org.springframework.http.HttpStatus;

public class IncorrectDateFormatException extends BadRequestException{
    public IncorrectDateFormatException(){
        super("Invalid date format. Use format - yyyy-MM-dd HH:mm:ss!");
    }
}

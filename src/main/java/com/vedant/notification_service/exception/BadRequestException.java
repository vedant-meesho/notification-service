package com.vedant.notification_service.exception;

import co.elastic.clients.elasticsearch.nodes.Http;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class BadRequestException extends RuntimeException{
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private String message;
    public BadRequestException(String message){
        super(message);
        this.message = message;
    }

}

package com.vedant.notification_service.controller;

import com.vedant.notification_service.constants.ErrorStatements;
import com.vedant.notification_service.exception.BadRequestException;
import com.vedant.notification_service.exception.InternalServerException;
import com.vedant.notification_service.model.requestBody.GetSmsBetweenDate;
import com.vedant.notification_service.model.requestBody.SearchWithText;
import com.vedant.notification_service.model.responseBody.ErrorResponse;
import com.vedant.notification_service.model.responseBody.SuccessResponse;
import com.vedant.notification_service.service.ElasticsearchService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.vedant.notification_service.constants.Constants.*;

@Slf4j
@RestController
public class ElasticsearchController {
    final ElasticsearchService elasticsearchService;

    public ElasticsearchController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @PostMapping("/v1/findSmsBetweenDate")
    public ResponseEntity<Object> getSmsBetweenDates(@RequestBody @Valid GetSmsBetweenDate request) {
        String methodName = "getSmsBetweenDates";
        HttpStatus httpStatus = HttpStatus.OK;
        Object response = new ErrorResponse("Unknown Error");
        try {
            log.info(REQUEST_LOG, methodName, request);
            Object result = elasticsearchService.getSmsBetweenDates(request);
            response = new SuccessResponse(result);
            log.info(RESPONSE_LOG, methodName, response);
        } catch(BadRequestException ex){
            log.error(BAD_REQUEST_LOG, methodName, request, ex.getStackTrace());
            httpStatus = HttpStatus.BAD_REQUEST;
            response = new ErrorResponse(ex.getMessage());
        } catch (InternalServerException ex) {
            log.error(INTERNAL_SERVER_LOG, methodName, request, ex.getStackTrace());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = new ErrorResponse(ErrorStatements.INTERNAL_SERVER_ERROR + " " + ex.getMessage());
        } catch (Exception ex){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(EXCEPTION_LOG, methodName, request, ex.getStackTrace());
        }
        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("/v1/searchText")
    public ResponseEntity<Object> searchWithText(@RequestBody @Valid SearchWithText request) {
        String methodName = "searchWithText";
        HttpStatus httpStatus = HttpStatus.OK;
        Object response = new ErrorResponse("Unknown Error");
        try {
            log.info(REQUEST_LOG, methodName, request);
            Object result = elasticsearchService.getSmsWithText(request.getText(), request.getPageNumber());
            response = new SuccessResponse(result);
            log.info(RESPONSE_LOG, methodName, response);
        } catch(BadRequestException ex){
            log.error(BAD_REQUEST_LOG, methodName, request, ex.getStackTrace());
            response = new ErrorResponse(ex.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (InternalServerException ex) {
            log.error(INTERNAL_SERVER_LOG, methodName, request, ex.getStackTrace());
            response = new ErrorResponse(ErrorStatements.INTERNAL_SERVER_ERROR + " " + ex.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception ex){
            log.error(EXCEPTION_LOG, methodName, request, ex.getStackTrace());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/v1/deleteEsIndex")
    public ResponseEntity<Object> deleteIndex(){
        elasticsearchService.deleteIndex();
        return ResponseEntity.ok("Successfully deleted");
    }

    @PostMapping("/v1/createEsIndex")
    public ResponseEntity<Object> createIndex(){
        elasticsearchService.createIndex();
        return ResponseEntity.ok("Successfully created");
    }
}

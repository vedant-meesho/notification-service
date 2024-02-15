package com.vedant.notification_service.controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.vedant.notification_service.constants.ErrorStatements;
import com.vedant.notification_service.constants.KafkaConstants;
import com.vedant.notification_service.exception.BadRequestException;
import com.vedant.notification_service.exception.InternalServerException;
import com.vedant.notification_service.exception.SmsRequestNotFoundException;
import com.vedant.notification_service.model.entity.ElasticsearchSms;
import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.model.entity.SmsRequest;
import com.vedant.notification_service.model.requestBody.GetSmsBetweenDate;
import com.vedant.notification_service.model.requestBody.SearchWithText;
import com.vedant.notification_service.model.requestBody.SendSmsRequest;
import com.vedant.notification_service.model.responseBody.ErrorResponse;
import com.vedant.notification_service.model.responseBody.sms.SmsError;
import com.vedant.notification_service.model.responseBody.sms.SmsSuccess;
import com.vedant.notification_service.model.responseBody.SuccessResponse;
import com.vedant.notification_service.repository.SmsRequestRepo;
import com.vedant.notification_service.service.ElasticsearchService;
import com.vedant.notification_service.service.SendSmsService;
import com.vedant.notification_service.service.kafka.producer.MessageProducer;
import com.vedant.notification_service.service.PhoneNumberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.vedant.notification_service.constants.Constants.*;

@Slf4j
@RestController
public class NotificationController {

    final PhoneNumberService phoneNumberService;

    final SendSmsService sendSmsService;

    public NotificationController(PhoneNumberService phoneNumberService, SendSmsService sendSmsService) {
        this.phoneNumberService = phoneNumberService;
        this.sendSmsService = sendSmsService;
    }

    @GetMapping("/v1/sms")
    public ResponseEntity<?>getAllSms(){
        String methodName = "getAllSms";
        HttpStatus httpStatus = HttpStatus.OK;
        Object response;
        try{
            log.info(REQUEST_LOG, methodName, methodName);
            Iterable<SmsRequest> result = sendSmsService.getAllSms();
            response = new SuccessResponse(result);
            log.info(RESPONSE_LOG, methodName, response);
        } catch (InternalServerException ex){
            log.info(INTERNAL_SERVER_LOG, methodName, methodName, ex.getStackTrace());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = new ErrorResponse(new SmsError(ErrorStatements.INTERNAL_SERVER_ERROR_CODE, ErrorStatements.INTERNAL_SERVER_ERROR + " " + ex.getMessage()));
        } catch (Exception ex){
            log.info(EXCEPTION_LOG, methodName, methodName, ex.getStackTrace());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = new ErrorResponse(new SmsError(ErrorStatements.INTERNAL_SERVER_ERROR_CODE, ErrorStatements.UNKNOWN_ERROR + ex.getMessage()));
        }
        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping(path = "/v1/sms/send")
    public ResponseEntity<?> sendSms(@RequestBody @Valid SendSmsRequest request){
        String methodName = "sendSms";
        HttpStatus httpStatus = HttpStatus.OK;
        Object response;
        try{
            log.info(REQUEST_LOG, methodName, request);
            Integer id = sendSmsService.sendSms(request);
            response = new SuccessResponse(new SmsSuccess(id,"Request Sent Successfully"));
            log.info(RESPONSE_LOG, methodName, response);
        } catch (BadRequestException ex){
            log.error(BAD_REQUEST_LOG, methodName, request, ex.getStackTrace());
            httpStatus = HttpStatus.BAD_REQUEST;
            response = new ErrorResponse(new SmsError(ErrorStatements.INVALID_REQUEST_ERROR_CODE, ex.getMessage()));
        } catch (Exception ex){
            log.error(EXCEPTION_LOG, methodName, request, ex.getStackTrace());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = new ErrorResponse(new SmsError(ErrorStatements.UNKNOWN_ERROR, ErrorStatements.UNKNOWN_ERROR + " " + ex.getMessage()));
        }
        return ResponseEntity.status(httpStatus).body(response);
    }

    @GetMapping("/v1/sms/{id}")
    public ResponseEntity<?> getSmsByRequestId(@PathVariable int id){
        String methodName = "getSmsByRequestId";
        HttpStatus httpStatus = HttpStatus.OK;
        Object response;
        try{
            log.info(REQUEST_LOG, methodName, id);
            SmsRequest request = sendSmsService.getSmsById(id);
            response = new SuccessResponse(request);
            log.info(RESPONSE_LOG, methodName, response);
        } catch (SmsRequestNotFoundException ex){
            log.error(EXECUTION_EXCEPTION_LOG, methodName, id, ex.getStackTrace());
            httpStatus = HttpStatus.NOT_FOUND;
            response = new ErrorResponse(new SmsError(ErrorStatements.INVALID_REQUEST_ERROR_CODE,"request_id not found"));
        }
        catch (Exception ex){
            log.error(EXCEPTION_LOG, methodName, id, ex.getStackTrace());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = new ErrorResponse(new SmsError(ErrorStatements.INTERNAL_SERVER_ERROR_CODE, ErrorStatements.UNKNOWN_ERROR + " " + ex.getMessage()));
        }
        return ResponseEntity.status(httpStatus).body(response);

    }
}

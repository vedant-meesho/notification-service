package com.vedant.notification_service.controller;

import com.vedant.notification_service.constants.ErrorStatements;
import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.model.requestBody.PhoneNumberConfigRequest;
import com.vedant.notification_service.model.responseBody.ErrorResponse;
import com.vedant.notification_service.model.responseBody.SuccessResponse;
import com.vedant.notification_service.model.responseBody.phone_number.NumbersConfig;
import com.vedant.notification_service.service.PhoneNumberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Arrays;

import static com.vedant.notification_service.constants.Constants.EXCEPTION_LOG;
import static com.vedant.notification_service.constants.Constants.REQUEST_LOG;

@Slf4j
@RestController
public class PhoneNumberController {
    @Autowired
    PhoneNumberService phoneNumberService;

    @GetMapping("/v1/blacklist")
    public ResponseEntity<?> blackListedNumbers(){
        String methodName = "blacklistedNumbers";
        try{
            log.info(REQUEST_LOG, methodName, methodName);
            Iterable<String> result = phoneNumberService.getBlacklistedNumbers();
            System.out.println(result);
            return ResponseEntity.ok(new SuccessResponse(result));
        } catch (Exception ex){
            log.error(EXCEPTION_LOG, methodName, methodName, ex.getStackTrace());
            return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorStatements.UNKNOWN_ERROR));
        }
    }

    @PostMapping("/v1/blacklist")
    public ResponseEntity<?> blackListNumbers(@RequestBody @Valid PhoneNumberConfigRequest request){
        String methodName = "blacklistNumbers";
        try{
            log.info(REQUEST_LOG, methodName, request);
            NumbersConfig response = phoneNumberService.blacklistNumbers(request.getPhoneNumbers());
            return ResponseEntity.ok(new SuccessResponse(response));
        } catch (Exception ex){
            log.error(EXCEPTION_LOG, methodName, request, ex.getStackTrace());
            return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorStatements.UNKNOWN_ERROR));
        }
    }

    @DeleteMapping("/v1/blacklist")
    public ResponseEntity<?> whiteListNumbers(@RequestBody @Valid PhoneNumberConfigRequest request){
        String methodName = "whiteListNumbers";
        try{
            log.info(REQUEST_LOG, methodName, request);
            NumbersConfig response = phoneNumberService.whitelistNumbers(request.getPhoneNumbers());
            return ResponseEntity.ok(new SuccessResponse(response));
        } catch (Exception ex){
            log.error(EXCEPTION_LOG, methodName, request, ex.getStackTrace());
            return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorStatements.UNKNOWN_ERROR));
        }
    }
}

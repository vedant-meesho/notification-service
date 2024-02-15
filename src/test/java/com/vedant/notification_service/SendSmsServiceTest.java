package com.vedant.notification_service;

import com.vedant.notification_service.exception.BadRequestException;
import com.vedant.notification_service.exception.SmsRequestNotFoundException;
import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.model.entity.SmsRequest;
import com.vedant.notification_service.model.requestBody.SendSmsRequest;
import com.vedant.notification_service.repository.BlackListRepo;
import com.vedant.notification_service.repository.SmsRequestRepo;
import com.vedant.notification_service.service.CachingService;
import com.vedant.notification_service.service.PhoneNumberService;
import com.vedant.notification_service.service.SendSmsService;
import com.vedant.notification_service.service.kafka.producer.MessageProducer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SendSmsServiceTest {
    @MockBean
    BlackListRepo blackListRepo;

    @MockBean
    CachingService cachingService;
    PhoneNumber blacklistedPhoneNumber = new PhoneNumber();
    PhoneNumber whitelistedPhoneNumber = new PhoneNumber();

    @MockBean
    SmsRequestRepo smsRequestRepo;

    @MockBean
    MessageProducer messageProducer;

    @Autowired
    SendSmsService sendSmsService;

    SendSmsRequest sendSmsRequest = new SendSmsRequest();

    SmsRequest smsRequest = new SmsRequest();

    @BeforeEach
    public void beforeEach2(){
        blacklistedPhoneNumber.setNumber("+919876543210");
        blacklistedPhoneNumber.setCreated_at(new Timestamp(System.currentTimeMillis()));

        whitelistedPhoneNumber.setNumber("+919876543211");
        whitelistedPhoneNumber.setCreated_at(new Timestamp(System.currentTimeMillis()));

        sendSmsRequest.setMessage("First Message");
        sendSmsRequest.setPhoneNumber(whitelistedPhoneNumber.getNumber());

        LocalDateTime localTime = LocalDateTime.now();

        smsRequest = SmsRequest.builder()
                .message(smsRequest.getMessage())
                .phone_number(whitelistedPhoneNumber.getNumber())
                .created_at(localTime)
                .updated_at(localTime)
                .build();
    }

    @Test
    @DisplayName("Sending SMS to blacklisted number Error")
    public void test11(){
        sendSmsRequest.setPhoneNumber(blacklistedPhoneNumber.getNumber());

        when(cachingService.checkNumber(blacklistedPhoneNumber.getNumber())).thenReturn(blacklistedPhoneNumber);

        Exception exception = assertThrows(BadRequestException.class, () ->
                sendSmsService.sendSms(sendSmsRequest)
        );

        String expectedMessage = "Phone number is blacklisted!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Sending SMS")
    public void test12(){
        when(cachingService.checkNumber(sendSmsRequest.getPhoneNumber())).thenReturn(null);

        sendSmsService.sendSms(sendSmsRequest);
    }

    @Test
    @DisplayName("Retrieving SMS by ID")
    public void test13(){
        when(smsRequestRepo.findById(smsRequest.getId())).thenReturn(Optional.of(smsRequest));

        SmsRequest response = sendSmsService.getSmsById(smsRequest.getId());

        assertEquals(smsRequest, response);
    }

    @Test
    @DisplayName("Retrieving SMS by invalid ID")
    public void test14(){
        when(smsRequestRepo.findById(smsRequest.getId()+1)).thenReturn(null);

        Exception exception = assertThrows(SmsRequestNotFoundException.class, () ->
                sendSmsService.getSmsById(smsRequest.getId() + 1)
        );

        String expectedMessage = "Request ID not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

package com.vedant.notification_service.service;

import com.vedant.notification_service.constants.KafkaConstants;
import com.vedant.notification_service.exception.BadRequestException;
import com.vedant.notification_service.exception.InternalServerException;
import com.vedant.notification_service.exception.SmsRequestNotFoundException;
import com.vedant.notification_service.model.entity.ElasticsearchSms;
import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.model.entity.SmsRequest;
import com.vedant.notification_service.model.requestBody.SendSmsRequest;
import com.vedant.notification_service.model.responseBody.ErrorResponse;
import com.vedant.notification_service.model.responseBody.sms.SmsError;
import com.vedant.notification_service.repository.SmsRequestRepo;
import com.vedant.notification_service.service.kafka.producer.MessageProducer;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SendSmsService {
    final MessageProducer messageProducer;
    final SmsRequestRepo smsRequestRepo;

    @Autowired
    CachingService cachingService;

    public SendSmsService(MessageProducer messageProducer, SmsRequestRepo smsRequestRepo) {
        this.messageProducer = messageProducer;
        this.smsRequestRepo = smsRequestRepo;
    }

    public Integer sendSms(SendSmsRequest smsRequestBody){
        String phoneNumber = smsRequestBody.getPhoneNumber();
        String message = smsRequestBody.getMessage();

        LocalDateTime currentTime = LocalDateTime.now();

        SmsRequest smsRequest = SmsRequest.builder()
                .phone_number(phoneNumber)
                .message(message)
                .created_at(currentTime)
                .updated_at(currentTime)
                .build();

        PhoneNumber isBlacklisted = cachingService.checkNumber(phoneNumber);
        if(isBlacklisted != null){
            throw new BadRequestException("Phone number is blacklisted!");
        }
        try{
            smsRequestRepo.save(smsRequest);
            messageProducer.sendMessage(KafkaConstants.KAFKA_SMS_REQUEST_TOPIC, smsRequest.getId());
            return smsRequest.getId();
        } catch (Exception ex){
            System.out.println(ex);
            throw new InternalServerException("Internal server error!");
        }
    }

    public SmsRequest getSmsById(int id){
        try{
            Optional<SmsRequest> smsRequestOptional = smsRequestRepo.findById(id);
            if(smsRequestOptional.isEmpty()){
                throw new SmsRequestNotFoundException("Request ID not found!");
            }
            return smsRequestOptional.get();
        } catch(SmsRequestNotFoundException ex){
            System.out.println("SMS Request with given ID not found");
            throw ex;
        } catch(Exception ex){
            System.out.println("Unknown error");
            System.out.println(ex);
            throw new InternalServerException("Unable to connect to MySQL.");
        }
    }

    public Iterable<SmsRequest> getAllSms(){
        try{
            return smsRequestRepo.findAll();
        } catch (Exception ex){
            System.out.println(ex);
            throw new InternalServerException("Unknown error occurred. Unable to fetch data!");
        }
    }
}

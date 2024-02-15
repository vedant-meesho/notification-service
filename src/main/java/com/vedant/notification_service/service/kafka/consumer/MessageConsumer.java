package com.vedant.notification_service.service.kafka.consumer;

import com.vedant.notification_service.constants.KafkaConstants;
import com.vedant.notification_service.constants.SmsSenderConstants;
import com.vedant.notification_service.exception.InternalServerException;
import com.vedant.notification_service.handler.SmsSender;
import com.vedant.notification_service.model.entity.SmsRequest;
import com.vedant.notification_service.model.entity.smsSender.response.ResponseItem;
import com.vedant.notification_service.repository.SmsRequestRepo;
import com.vedant.notification_service.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class MessageConsumer {
    final SmsRequestRepo smsRequestRepo;

    final
    SmsSender smsSender;

    final
    ElasticsearchService elasticsearchService;

    public MessageConsumer(SmsRequestRepo smsRequestRepo, ElasticsearchService elasticsearchService, SmsSender smsSender) {
        this.smsRequestRepo = smsRequestRepo;
        this.elasticsearchService = elasticsearchService;
        this.smsSender = smsSender;
    }

    @KafkaListener(topics = KafkaConstants.KAFKA_SMS_REQUEST_TOPIC, groupId = KafkaConstants.KAFKA_SMS_REQUEST_GROUP_ID)
    public void listenGroupFoo(int id) throws IOException {
        Optional<SmsRequest> smsRequestResult = smsRequestRepo.findById(id);
        if(smsRequestResult.isEmpty()){
            System.out.println(id + " not found. Unable to send sms!");
            return;
        }

        ResponseItem response = smsSender.sendSms(smsRequestResult.get());

        SmsRequest smsRequest = smsRequestResult.get();
        smsRequest.setUpdated_at(LocalDateTime.now());
        try{
            if(!Objects.equals(response.getCode(), SmsSenderConstants.SUCCESS_RESPONSE_CODE)){
                smsRequest.setStatus("Failed");
                smsRequest.setFailure_code(response.getCode());
                smsRequest.setFailure_comments(response.getDescription());
                System.out.println("Code error");
            } else{
                smsRequest.setStatus("sent");
                System.out.println("sms to " + smsRequest);
                elasticsearchService.saveData(smsRequest);
            }
        } catch(InternalServerException ex){
            smsRequest.setStatus("Failed");
            smsRequest.setFailure_code("500");
            smsRequest.setFailure_comments("Unkown server errror");
            System.out.println("Internal server exception occurred");
            System.out.println(ex);
        } finally {
            smsRequestRepo.save(smsRequest);
        }
    }
}
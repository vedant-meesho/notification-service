package com.vedant.notification_service.service.kafka.producer;

import com.vedant.notification_service.constants.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class MessageProducer {

    private final KafkaTemplate<String, Integer> kafkaTemplate;

    public MessageProducer(KafkaTemplate<String, Integer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, int message) {
        CompletableFuture<SendResult<String, Integer>> future = kafkaTemplate.send(KafkaConstants.KAFKA_SMS_REQUEST_TOPIC, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }
}
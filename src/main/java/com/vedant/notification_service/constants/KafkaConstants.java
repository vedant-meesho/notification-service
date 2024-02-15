package com.vedant.notification_service.constants;

import org.springframework.stereotype.Component;

@Component
public class KafkaConstants {
    public static final String KAFKA_SMS_REQUEST_TOPIC = "notification.send_sms";
    public static final String KAFKA_SMS_REQUEST_GROUP_ID = "notification-consumer";
    public static final String KAFKA_BOOTSTRAP_SERVER_ADDRESS = "localhost:9092";

    private KafkaConstants(){};
}

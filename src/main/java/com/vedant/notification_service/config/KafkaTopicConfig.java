package com.vedant.notification_service.config;

import com.vedant.notification_service.constants.KafkaConstants;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaTopicConfig {
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BOOTSTRAP_SERVER_ADDRESS);
        return new KafkaAdmin(configs);
    }

//    @Bean
//    public NewTopic topic1() {
//        return new NewTopic("notification.send_sms", 1, (short) 1);
//    }
}
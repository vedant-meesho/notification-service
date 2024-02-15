package com.vedant.notification_service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.vedant.notification_service.exception.BadRequestException;
import com.vedant.notification_service.exception.IncorrectDateFormatException;
import com.vedant.notification_service.exception.PageNumberNegativeException;
import com.vedant.notification_service.exception.SmsRequestNotFoundException;
import com.vedant.notification_service.helper.ElasticsearchTestHelper;
import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.model.entity.SmsRequest;
import com.vedant.notification_service.model.requestBody.GetSmsBetweenDate;
import com.vedant.notification_service.model.requestBody.SendSmsRequest;
import com.vedant.notification_service.model.responseBody.phone_number.NumbersConfig;
import com.vedant.notification_service.repository.BlackListRepo;
import com.vedant.notification_service.repository.SmsRequestRepo;
import com.vedant.notification_service.service.CachingService;
import com.vedant.notification_service.service.ElasticsearchService;
import com.vedant.notification_service.service.PhoneNumberService;
import com.vedant.notification_service.service.SendSmsService;
import com.vedant.notification_service.service.kafka.producer.MessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ElasticsearchServiceTest.class,
        PhoneNumberServiceTest.class,
        SendSmsServiceTest.class
})
class NotificationServiceApplicationTests {
    @Test
    public void contextLoads() {
    }
}
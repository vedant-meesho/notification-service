package com.vedant.notification_service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.vedant.notification_service.exception.BadRequestException;
import com.vedant.notification_service.exception.IncorrectDateFormatException;
import com.vedant.notification_service.exception.PageNumberNegativeException;
import com.vedant.notification_service.model.requestBody.GetSmsBetweenDate;
import com.vedant.notification_service.service.ElasticsearchService;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ElasticsearchServiceTest {
    @MockBean
    ElasticsearchClient elasticsearchClient;

    @Autowired
    ElasticsearchService elasticsearchService;

    GetSmsBetweenDate request = new GetSmsBetweenDate();

    @BeforeEach
    public void beforeEach(){
        String phoneNumber = "+918085836022";
        String startDate = "2024-01-03 22:21:04";
        String endDate = "2024-01-03 22:21:04";
        int pageNumber = 0;
        request.setPhoneNumber(phoneNumber);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setPageNumber(pageNumber);
    }

    @Test
    @DisplayName("Negative page number Error in Between Date Search")
    public void test1(){
        request.setPageNumber(-1);

        Exception exception = assertThrows(PageNumberNegativeException.class, () ->
                elasticsearchService.getSmsBetweenDates(request)
        );

        String expectedMessage = "Page number cannot be negative!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Date Time Format Error in Between Date Search")
    public void test2(){
        request.setStartDate("2024-01-0322:21:04");

        Exception exception = assertThrows(IncorrectDateFormatException.class, () ->
                elasticsearchService.getSmsBetweenDates(request)
        );

        String expectedMessage = "Invalid date format. Use format - yyyy-MM-dd HH:mm:ss!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("End date null Error in Between Date Search")
    public void test3(){
        request.setEndDate(null);

        Exception exception = assertThrows(BadRequestException.class, () ->
                elasticsearchService.getSmsBetweenDates(request)
        );

        String expectedMessage = "Insufficient Data provided.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Start date null Error in Between Date Search")
    public void test4(){
        request.setStartDate(null);

        Exception exception = assertThrows(BadRequestException.class, () ->
                elasticsearchService.getSmsBetweenDates(request)
        );

        String expectedMessage = "Insufficient Data provided.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Phone number null Error in Between Date Search")
    public void test5(){
        request.setPhoneNumber(null);

        Exception exception = assertThrows(BadRequestException.class, () ->
                elasticsearchService.getSmsBetweenDates(request)
        );

        String expectedMessage = "Insufficient Data provided.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Page number null Error in Between Date Search")
    public void test6(){
        request.setPhoneNumber("");

        Exception exception = assertThrows(BadRequestException.class, () ->
                elasticsearchService.getSmsBetweenDates(request)
        );

        String expectedMessage = "Insufficient Data provided.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Phone number Empty Error in Between Date Search")
    public void test7(){
        request.setPhoneNumber("");

        Exception exception = assertThrows(BadRequestException.class, () ->
                elasticsearchService.getSmsBetweenDates(request)
        );

        String expectedMessage = "Insufficient Data provided.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

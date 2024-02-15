package com.vedant.notification_service;

import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.model.responseBody.phone_number.NumbersConfig;
import com.vedant.notification_service.repository.BlackListRepo;
import com.vedant.notification_service.service.CachingService;
import com.vedant.notification_service.service.PhoneNumberService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PhoneNumberServiceTest {

    @MockBean
    BlackListRepo blackListRepo;

    @MockBean
    CachingService cachingService;

    @Autowired
    PhoneNumberService phoneNumberService;

    PhoneNumber blacklistedPhoneNumber = new PhoneNumber();
    PhoneNumber whitelistedPhoneNumber = new PhoneNumber();

    @BeforeEach
    public void beforeEach1(){
        blacklistedPhoneNumber.setNumber("+919876543210");
        blacklistedPhoneNumber.setCreated_at(new Timestamp(System.currentTimeMillis()));

        whitelistedPhoneNumber.setNumber("+919876543211");
        whitelistedPhoneNumber.setCreated_at(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    @DisplayName("All Blacklisted Numbers")
    public void test8(){
        when(blackListRepo.findAll()).thenReturn(List.of(new PhoneNumber[]{blacklistedPhoneNumber}));

        Iterable<String> expected = List.of("+919876543210");

        Iterable<String> actual = phoneNumberService.getBlacklistedNumbers();
        assertEquals(expected, actual);

        verify(blackListRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("Blacklist Given Numbers")
    public void test9(){;
        NumbersConfig response = phoneNumberService.blacklistNumbers(List.of(whitelistedPhoneNumber.getNumber()).toArray(new String[0]));

        NumbersConfig expectedResponse = new NumbersConfig(List.of(whitelistedPhoneNumber.getNumber()), List.of());

        assertEquals(expectedResponse.getSuccessful(), response.getSuccessful());
        assertEquals(expectedResponse.getFailed(), response.getFailed());
    }

    @Test
    @DisplayName("Whitelist Given Numbers")
    public void test10(){
        NumbersConfig response = phoneNumberService.whitelistNumbers(List.of(blacklistedPhoneNumber.getNumber()).toArray(new String[0]));

        NumbersConfig expectedResponse = new NumbersConfig(List.of(blacklistedPhoneNumber.getNumber()), List.of());

        assertEquals(expectedResponse.getSuccessful(), response.getSuccessful());
        assertEquals(expectedResponse.getFailed(), response.getFailed());

        verify(cachingService, times(1)).deleteNumber(blacklistedPhoneNumber.getNumber());
    }
}

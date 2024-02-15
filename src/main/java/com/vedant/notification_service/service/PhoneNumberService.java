package com.vedant.notification_service.service;

import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.model.responseBody.phone_number.NumbersConfig;
import com.vedant.notification_service.repository.BlackListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PhoneNumberService {
    @Autowired
    BlackListRepo blackListRepo;

    @Autowired
    CachingService cachingService;

    public Iterable<String> getBlacklistedNumbers(){
        Iterable<String> res = blackListRepo.findAll().stream().map(PhoneNumber::getNumber).toList();
        System.out.println(res);
        return res;
    }

    public NumbersConfig blacklistNumbers(String[] phoneNumbers){
        List<String> successful = new ArrayList<>(), failed = new ArrayList<>();
        Arrays.stream(phoneNumbers).forEach(number -> {
            PhoneNumber data = new PhoneNumber();
            data.setNumber(number);
            data.setCreated_at(new Timestamp(System.currentTimeMillis()));
            try{
                cachingService.putNumber(data);
                successful.add(number);
            } catch (Exception ex){
                failed.add(number + " failed due to " + ex.getClass().getSimpleName());
            }
        });
        return new NumbersConfig(successful,failed);
    }

    public NumbersConfig whitelistNumbers(String[] phoneNumbers){
        List<String> successful = new ArrayList<>(), failed = new ArrayList<>();
        Arrays.stream(phoneNumbers).forEach(number -> {
            try{
                cachingService.deleteNumber(number);
                successful.add(number);
            } catch (Exception ex){
                System.out.println(ex);
                failed.add(number + " failed due to " + ex.getClass().getSimpleName());
            }
        });
        return new NumbersConfig(successful,failed);
    }

    public PhoneNumber savePhoneNumber(PhoneNumber phoneNumber){
        return blackListRepo.save(phoneNumber);
    }
}

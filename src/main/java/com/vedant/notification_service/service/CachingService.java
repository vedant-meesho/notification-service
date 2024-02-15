package com.vedant.notification_service.service;

import com.vedant.notification_service.model.entity.PhoneNumber;
import com.vedant.notification_service.repository.BlackListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CachingService {
    @Autowired
    BlackListRepo blackListRepo;

    @CachePut(value="Blacklist", key = "#phoneNumber.number")
    public PhoneNumber putNumber(PhoneNumber phoneNumber){
        blackListRepo.save(phoneNumber);
        return phoneNumber;
    }

    @Cacheable(value="Blacklist", key = "#phoneNumber")
    public PhoneNumber checkNumber(String phoneNumber){
        return blackListRepo.findById(phoneNumber).orElse(null);
    }

    @CacheEvict(value = "Blacklist", key="#number")
    public void deleteNumber(String number){
        PhoneNumber phoneNumber = blackListRepo.findById(number).orElse(null);
        assert phoneNumber != null;
        blackListRepo.delete(phoneNumber);
    }

}

package com.vedant.notification_service.repository;

import com.vedant.notification_service.model.entity.SmsRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRequestRepo extends CrudRepository<SmsRequest, Integer> {
}

package com.vedant.notification_service.repository;

import com.vedant.notification_service.model.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepo extends JpaRepository<PhoneNumber, String> {
}

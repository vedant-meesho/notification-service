package com.vedant.notification_service.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "sms-request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Document(indexName = "sms_request")
public class SmsRequest {
    @Id
    @GeneratedValue
    private int id;
    private String phone_number;
    private String message;
    private String status;
    private String failure_code;
    private String failure_comments;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Override
    public String toString() {
        return "SmsRequest{" +
                "id=" + id +
                ", phone_number='" + phone_number + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", failure_code='" + failure_code + '\'' +
                ", failure_comments='" + failure_comments + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}

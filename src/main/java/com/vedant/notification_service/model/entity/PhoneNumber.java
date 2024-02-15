package com.vedant.notification_service.model.entity;


import co.elastic.clients.util.DateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PhoneNumber implements Serializable {

    @Serial
    private static final long serialVersionUID = -4439114469417994311L;

    @Id
    private String number;
    private Timestamp created_at;
}

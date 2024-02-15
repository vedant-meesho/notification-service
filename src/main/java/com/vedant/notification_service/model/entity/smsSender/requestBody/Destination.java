package com.vedant.notification_service.model.entity.smsSender.requestBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Destination {
    private List<String> msisdn;
    private String correlationid;

    @Override
    public String toString() {
        return "Destination{" +
                "msisdn=" + msisdn +
                ", correlationId='" + correlationid + '\'' +
                '}';
    }
}

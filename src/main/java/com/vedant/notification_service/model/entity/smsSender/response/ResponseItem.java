package com.vedant.notification_service.model.entity.smsSender.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseItem {
    private String code;
    private String transid;
    private String description;
    private String correlationid;

    @Override
    public String toString() {
        return "ResponseItem{" +
                "code='" + code + '\'' +
                ", transid='" + transid + '\'' +
                ", description='" + description + '\'' +
                ", correlationid='" + correlationid + '\'' +
                '}';
    }
}

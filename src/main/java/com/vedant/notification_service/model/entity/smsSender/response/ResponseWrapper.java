package com.vedant.notification_service.model.entity.smsSender.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper {
    private List<ResponseItem> response;

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "response=" + response +
                '}';
    }
}

package com.vedant.notification_service.model.entity.smsSender.requestBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    private String text;

    @Override
    public String toString() {
        return "Channel{" +
                "text='" + text + '\'' +
                '}';
    }
}

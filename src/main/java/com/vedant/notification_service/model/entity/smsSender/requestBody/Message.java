package com.vedant.notification_service.model.entity.smsSender.requestBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String deliverychannel;
    private Map<String, Channel> channels;
    private List<Destination> destination;

    @Override
    public String toString() {
        return "Message{" +
                "deliveryChannel='" + deliverychannel + '\'' +
                ", channels=" + channels +
                ", destination=" + destination +
                '}';
    }
}

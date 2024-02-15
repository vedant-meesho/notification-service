package com.vedant.notification_service.model.responseBody.sms;

import com.vedant.notification_service.model.responseBody.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsSuccess implements Response {
    private int id;
    private String comments;
}

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
public class SmsError implements Response {
    private String code;
    private String message;
}

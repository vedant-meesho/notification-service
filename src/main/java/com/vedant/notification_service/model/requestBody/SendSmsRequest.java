package com.vedant.notification_service.model.requestBody;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsRequest {
    @NotNull(message = "Phone number should not be null!")
    @NotEmpty(message = "Phone number should not be empty!")
    private String phoneNumber;
    @NotNull(message = "Message should not be null!")
    @NotEmpty(message = "Message should not be empty!")
    private String message;
}

package com.vedant.notification_service.model.requestBody;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberConfigRequest {
    @NotNull(message = "Phone numbers list should not be null")
    @NotEmpty(message = "Phone numbers list should not be empty")
    private String[] phoneNumbers;
}

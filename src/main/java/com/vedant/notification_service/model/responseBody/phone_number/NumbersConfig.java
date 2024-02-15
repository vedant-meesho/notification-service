package com.vedant.notification_service.model.responseBody.phone_number;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumbersConfig {
    private List<String> successful;
    private List<String> failed;
}

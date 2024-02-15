package com.vedant.notification_service.model.requestBody;

import co.elastic.clients.util.DateTime;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class GetSmsBetweenDate {
    @NotNull(message = "Phone number should not be null!")
    private String phoneNumber;
    @NotNull(message = "Start date should not be null!")
    @NotEmpty(message = "Start Date should be provided!")
    private String startDate;
    @NotNull(message = "End date should not be null!")
    @NotEmpty(message = "End date should be provided!")
    private String endDate;
    @NotNull(message = "Page number should not be null!")
    private int pageNumber;
}

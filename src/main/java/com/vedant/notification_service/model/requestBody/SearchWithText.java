package com.vedant.notification_service.model.requestBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchWithText {
    @NotNull(message = "Search should not be null")
    @NotEmpty(message = "Search text is required!")
    private String text;
    @NotNull(message = "Page Number is required!")
    private Integer pageNumber;
}

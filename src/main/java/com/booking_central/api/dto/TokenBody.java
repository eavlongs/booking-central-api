package com.booking_central.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenBody {
    @NotNull
    @Max(value = 5000, message = "Token length is too long")
    @Min(value = 1, message = "Token length is too short")
    private String token;
}

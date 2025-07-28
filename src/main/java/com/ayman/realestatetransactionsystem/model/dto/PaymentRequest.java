package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull
    @Positive
    private Long transectionId;

    @NotEmpty
    @Size(max = 3,
            min = 3,
            message = "The cvv must be exactly 3 characters")
    private String cvv;

    @NotNull
    private LocalDate expiryDate;
}

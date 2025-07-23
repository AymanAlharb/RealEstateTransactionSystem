package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @Positive
    private Long transectionId;

    @Size(max = 3,
            min = 3,
            message = "The cvv must be exactly 3 characters")
    private String cvv;
}

package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBuyingPropertyRequest {
    @Positive
    private Long propertyId;
    @Positive
    private Long buyerId;
}

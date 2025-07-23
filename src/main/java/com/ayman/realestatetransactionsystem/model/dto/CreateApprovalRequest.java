package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateApprovalRequest {
    @Positive
    private Long transectionId;
    private Boolean approval;
    private String reasonOfFailure;
}

package com.ayman.realestatetransactionsystem.model.dto.response;

import com.ayman.realestatetransactionsystem.model.enums.TransactionStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class PropertyRequestResponse {
    private String propertyTitle;
    private String buyerUsername;
    private String sellerUsername;
    private String brokerUsername;
    private double amount;
    private TransactionStatusEnum status;
    private LocalDateTime createdAt;
}

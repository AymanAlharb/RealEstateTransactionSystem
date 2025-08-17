package com.ayman.realestatetransactionsystem.model.mapper;

import com.ayman.realestatetransactionsystem.model.dto.response.PropertyRequestResponse;
import com.ayman.realestatetransactionsystem.model.entity.Transaction;

import java.time.LocalDateTime;

public class PropertyRequestMapper {
    public static PropertyRequestResponse createPropertyRequestResponse(Transaction transaction) {
        return PropertyRequestResponse.builder()
                .propertyTitle(transaction.getProperty().getTitle())
                .buyerUsername(transaction.getBuyer().getUsername())
                .sellerUsername(transaction.getSeller().getUsername())
                .brokerUsername(transaction.getBroker().getUsername())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

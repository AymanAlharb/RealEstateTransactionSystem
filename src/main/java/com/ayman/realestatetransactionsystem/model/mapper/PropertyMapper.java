package com.ayman.realestatetransactionsystem.model.mapper;

import com.ayman.realestatetransactionsystem.model.dto.response.PropertyCreatedResponse;
import com.ayman.realestatetransactionsystem.model.dto.response.PropertyRequestResponse;
import com.ayman.realestatetransactionsystem.model.entity.Property;

import java.time.LocalDateTime;

public class PropertyMapper {
    public static PropertyCreatedResponse createPropertyCreatedResponse(Property property){
        return PropertyCreatedResponse.builder()
                .title(property.getTitle())
                .description(property.getDescription())
                .price(property.getPrice())
                .status(property.getStatus())
                .location(property.getLocation())
                .cityName(property.getCity().getName())
                .regionName(property.getCity().getRegion())
                .brokerUsername(property.getBroker().getUsername())
                .ownerUsername(property.getOwner().getUsername())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

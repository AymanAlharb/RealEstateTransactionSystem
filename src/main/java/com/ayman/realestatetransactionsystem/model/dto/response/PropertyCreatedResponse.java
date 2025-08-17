package com.ayman.realestatetransactionsystem.model.dto.response;
import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Builder
@Data
public class PropertyCreatedResponse {

    private String title;
    private String description;
    private double price;
    private PropertyStatusEnum status;
    private String location;
    private String cityName;
    private String regionName;
    private String brokerUsername;
    private String ownerUsername;
    private LocalDateTime createdAt;
}

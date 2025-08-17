package com.ayman.realestatetransactionsystem.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CityCreatedResponse {
    private String cityName;
    private String regionName;
    private LocalDateTime createdAt;
}

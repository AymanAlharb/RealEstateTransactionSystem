package com.ayman.realestatetransactionsystem.model.mapper;

import com.ayman.realestatetransactionsystem.model.dto.response.CityCreatedResponse;
import com.ayman.realestatetransactionsystem.model.entity.City;

import java.time.LocalDateTime;

public class CityMapper {
    public static CityCreatedResponse createCityResponse(City city){
        return CityCreatedResponse.builder()
                .cityName(city.getName())
                .regionName(city.getRegion())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

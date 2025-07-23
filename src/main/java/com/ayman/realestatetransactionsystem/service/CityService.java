package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.exception.ApiException;
import com.ayman.realestatetransactionsystem.model.City;
import com.ayman.realestatetransactionsystem.model.dto.CreateCityRequest;
import com.ayman.realestatetransactionsystem.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CityService {
    private final CityRepository cityRepository;

    public void addCity(CreateCityRequest cityRequest) {
        if (cityRepository.getCityByNameAndRegion(cityRequest.getName(), cityRequest.getRegion()) != null)
            throw new ApiException("City exists in the system");
        City city = City.builder()
                .name(cityRequest.getName())
                .region(cityRequest.getRegion())
                .build();
        cityRepository.save(city);
    }
}

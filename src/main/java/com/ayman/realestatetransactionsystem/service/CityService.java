package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.dto.response.CityCreatedResponse;
import com.ayman.realestatetransactionsystem.model.entity.City;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateCityRequest;
import com.ayman.realestatetransactionsystem.model.entity.User;
import com.ayman.realestatetransactionsystem.repository.CityRepository;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.ayman.realestatetransactionsystem.model.mapper.CityMapper.createCityResponse;

@RequiredArgsConstructor
@Slf4j
@Service
public class CityService {
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;


    @Transactional
    public CityCreatedResponse addCity(CreateCityRequest cityRequest) {
        User user = userRepository.findUserByUsername
                (commonService.getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        if (cityRepository.getCityByNameAndRegion(cityRequest.getName(), cityRequest.getRegion()) != null) {
            log.info("User: {} tried to the existing city: {} in the region: {}",
                    user.getUsername(), cityRequest.getName(), cityRequest.getRegion());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "City exists in the system");
        }

        City city = City.builder()
                .name(cityRequest.getName())
                .region(cityRequest.getRegion())
                .build();
        cityRepository.save(city);

        log.info("User: {} added the city: {} in the region: {}",
                user.getUsername(), cityRequest.getName(), cityRequest.getRegion());

        return createCityResponse(city);
    }
}

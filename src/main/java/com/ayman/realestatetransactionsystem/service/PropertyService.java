package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.exception.ApiException;
import com.ayman.realestatetransactionsystem.model.City;
import com.ayman.realestatetransactionsystem.model.Property;
import com.ayman.realestatetransactionsystem.model.PropertyOwnership;
import com.ayman.realestatetransactionsystem.model.User;
import com.ayman.realestatetransactionsystem.model.dto.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import com.ayman.realestatetransactionsystem.repository.CityRepository;
import com.ayman.realestatetransactionsystem.repository.PropertyOwnerShipRepository;
import com.ayman.realestatetransactionsystem.repository.PropertyRepository;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final PropertyOwnerShipRepository propertyOwnerShipRepository;

    public void addProperty(CreatePropertyRequest propertyRequest) {
        // Get Broker username for logging
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new ApiException("Token not available");
        }

        Jwt jwt = jwtAuth.getToken();
        String brokerUsername = jwt.getClaim("preferred_username");

        // Get the user and check if the user in the system
        User user = getUserOrThrow(propertyRequest.getOwnerUsername());

        // Check if the user is a seller
        if (!user.getRole().equals(UserRoleEnum.SELLER))
            throw new ApiException("Properties can only be added to sellers");

        // Check if the city in the system
        City city = getCityOrThrow(propertyRequest.getCityName(), propertyRequest.getRegionName());

        // Create the propertyOwnership object
        PropertyOwnership propertyOwnership = PropertyOwnership.builder()
                .owner(user)
                .ownershipDate(LocalDateTime.now())
                .ownerFlag(true)
                .build();

        // Create the property and save it
        Property property = Property.builder()
                .title(propertyRequest.getTitle())
                .description(propertyRequest.getDescription())
                .price(propertyRequest.getPrice())
                .status(assignPropertyStatus(propertyRequest.getStatus()))
                .location(propertyRequest.getLocation())
                .city(city)
                .ownershipSet(Set.of(propertyOwnership))
                .build();

        propertyOwnership.setProperty(property);

        propertyRepository.save(property);
        log.info("Broker: {} added the property {} to {}", brokerUsername, property.getTitle(), user.getUsername());
    }

    private User getUserOrThrow(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException("No user with the username " + username + " exists in the system.");
        }
        return user;
    }

    private City getCityOrThrow(String cityName, String regionName) {
        City city = cityRepository.getCityByNameAndRegion(cityName, regionName);
        if (city == null)
            throw new ApiException("No city with the name " + cityName + " in the region " + regionName + " exists in the system.");
        return city;
    }

    private PropertyStatusEnum assignPropertyStatus(String status) {
        switch (status) {
            case "AVAILABLE" -> {
                return PropertyStatusEnum.AVAILABLE;
            }
            case "LOCKED" -> {
                return PropertyStatusEnum.LOCKED;
            }
            case "SOLD" -> {
                return PropertyStatusEnum.SOLD;
            }
            default -> {
                return PropertyStatusEnum.HIDDEN;
            }
        }
    }

}

package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.exception.ApiException;
import com.ayman.realestatetransactionsystem.model.City;
import com.ayman.realestatetransactionsystem.model.Property;
import com.ayman.realestatetransactionsystem.model.PropertyOwnership;
import com.ayman.realestatetransactionsystem.model.User;
import com.ayman.realestatetransactionsystem.model.dto.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.CreateUpdatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import com.ayman.realestatetransactionsystem.repository.CityRepository;
import com.ayman.realestatetransactionsystem.repository.PropertyOwnerShipRepository;
import com.ayman.realestatetransactionsystem.repository.PropertyRepository;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CommonService commonService;

    public void addProperty(CreatePropertyRequest propertyRequest) {
        // Get Broker username for logging
        String brokerUsername = commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication());
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
                .status(assignPropertyStatus(propertyRequest.getStatus().toUpperCase()))
                .location(propertyRequest.getLocation())
                .city(city)
                .ownershipSet(Set.of(propertyOwnership))
                .broker(userRepository.findUserByUsername(brokerUsername))
                .owner(user)
                .build();

        propertyOwnership.setProperty(property);

        propertyRepository.save(property);
        log.info("Broker: {} added the property {} to {}", brokerUsername, property.getTitle(), user.getUsername());
    }

    @Transactional
    public void deleteProperty(Long propertyId){
        // Check if the property exists and belongs to the seller
        Property property = validate(propertyId);

        // Delete
        propertyRepository.delete(property);
    }

    @Transactional
    public void updateProperty(Long propertyId, CreateUpdatePropertyRequest updatePropertyRequest){
        // Check if the property exists and belongs to the seller
        Property property = validate(propertyId);

        // Update
        property.setTitle(updatePropertyRequest.getTitle());
        property.setDescription(updatePropertyRequest.getDescription());
        property.setPrice(updatePropertyRequest.getPrice());
        property.setStatus(assignPropertyStatus(updatePropertyRequest.getStatus()));
        propertyRepository.save(property);
    }

    private Property validate(Long propertyId){
        // Get seller
        User seller = userRepository.findUserByUsername(commonService
                .getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Get property
        Property property = propertyRepository.findPropertyById(propertyId);
        if(property == null) throw new ApiException("No property with the id " + propertyId + " exists.");
        // Check if seller owns the property
        if(!property.getOwner().equals(seller))
            throw new ApiException("Seller does not owns the property.");

        return property;
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

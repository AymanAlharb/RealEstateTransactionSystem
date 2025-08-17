package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.dto.request.PropertyMessage;
import com.ayman.realestatetransactionsystem.model.dto.response.PropertyCreatedResponse;
import com.ayman.realestatetransactionsystem.model.entity.*;
import com.ayman.realestatetransactionsystem.model.dto.request.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateUpdatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import com.ayman.realestatetransactionsystem.properties.RabbitMQProperties;
import com.ayman.realestatetransactionsystem.repository.CityRepository;
import com.ayman.realestatetransactionsystem.repository.PropertyRepository;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Set;

import static com.ayman.realestatetransactionsystem.model.mapper.PropertyMapper.createPropertyCreatedResponse;

@RequiredArgsConstructor
@Slf4j
@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CommonService commonService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    @Transactional
    public PropertyCreatedResponse addProperty(CreatePropertyRequest propertyRequest) {
        // Get Broker username for logging
        String brokerUsername = commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication());

        // Get the owner and check if the owner in the system
        User user = getUserOrThrow(propertyRequest.getOwnerUsername());

        // Check if the owner is a seller
        if (!user.getRole().equals(UserRoleEnum.SELLER)) {
            log.info("User {} tried to add a property to the user: {}", brokerUsername, user.getUsername());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Properties can only be added to sellers");
        }

        // Check if the city in the system
        City city = getCityOrThrow(propertyRequest.getCityName(), propertyRequest.getRegionName(), brokerUsername);

        // Create the propertyOwnership object
        PropertyOwnership propertyOwnership = PropertyOwnership.builder()
                .owner(user)
                .ownershipDate(LocalDateTime.now())
                .ownerFlag(true)
                .build();
        log.info("Property ownership created successfully for the property: {} and the owner: {}", propertyRequest.getTitle(), propertyRequest.getOwnerUsername());

        // Create the property and save it
        Property property = Property.builder()
                .title(propertyRequest.getTitle())
                .description(propertyRequest.getDescription())
                .price(propertyRequest.getPrice())
                .status(PropertyStatusEnum.getCode(propertyRequest.getStatus()))
                .location(propertyRequest.getLocation())
                .city(city)
                .ownershipSet(Set.of(propertyOwnership))
                .broker(userRepository.findUserByUsername(brokerUsername))
                .owner(user)
                .build();

        propertyOwnership.setProperty(property);

        propertyRepository.save(property);
        log.info("Broker: {} added the property: {} to the user: {}", brokerUsername, property.getTitle(), user.getUsername());

        // Add to elasticsearch database
        pushToElasticsearch(property);

        return createPropertyCreatedResponse(property);
    }

    private void pushToElasticsearch(Property property) {
        PropertyMessage message = convertToMessage(property);
        rabbitTemplate.convertAndSend(rabbitMQProperties.getExchangeName(), rabbitMQProperties.getPropertyQueue().getRoutingKeyName(), message);
    }

    private PropertyMessage convertToMessage(Property property) {
        return PropertyMessage.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price(property.getPrice())
                .status(property.getStatus().toString())
                .location(property.getLocation())
                .city(property.getCity().getName())
                .region(property.getCity().getRegion())
                .ownerName(property.getOwner().getUsername())
                .build();
    }

    @Transactional
    public void deleteProperty(Long propertyId) {
        // Check if the property exists and belongs to the seller
        Property property = validate(propertyId, "delete");

        // Delete
        propertyRepository.delete(property);
        log.info("User: {} deleted the property: {}", property.getOwner().getUsername(), property.getTitle());
    }

    @Transactional
    public void updateProperty(Long propertyId, CreateUpdatePropertyRequest updatePropertyRequest) {
        // Check if the property exists and belongs to the seller
        Property property = validate(propertyId, "update");

        // Update
        property.setTitle(updatePropertyRequest.getTitle());
        property.setDescription(updatePropertyRequest.getDescription());
        property.setPrice(updatePropertyRequest.getPrice());
        property.setStatus(PropertyStatusEnum.getCode(updatePropertyRequest.getStatus()));
        propertyRepository.save(property);
        log.info("User: {} updated the property {}", property.getOwner().getUsername(), property);
    }

    private Property validate(Long propertyId, String operation) {
        // Get seller
        User seller = userRepository.findUserByUsername(commonService
                .getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Get property
        Property property = propertyRepository.findPropertyById(propertyId);
        if (property == null) {
            log.info("User: {} tried to {} a non existing property", operation, seller.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No property with the id " + propertyId + " exists.");
        }

        // Check if seller owns the property
        if (!property.getOwner().equals(seller)) {
            log.info("User: {} tried to {} a property they do not own", seller.getUsername(), operation);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seller does not owns the property.");
        }

        return property;
    }


    private User getUserOrThrow(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with the username " + username + " exists in the system.");
        }
        return user;
    }

    private City getCityOrThrow(String cityName, String regionName, String username) {
        City city = cityRepository.getCityByNameAndRegion(cityName, regionName);
        if (city == null) {
            log.info("User: {} tried to add a property to non existing city", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No city with the name " + cityName + " in the region " + regionName + " exists in the system.");
        }
        return city;
    }
}

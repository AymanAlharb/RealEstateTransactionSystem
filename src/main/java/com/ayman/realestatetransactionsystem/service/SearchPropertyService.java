package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.Property;
import com.ayman.realestatetransactionsystem.model.PropertyDocument;
import com.ayman.realestatetransactionsystem.repository.PropertyElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class SearchPropertyService {

    private final PropertyElasticsearchRepository propertyElasticsearchRepository;

    public void createProperty(Property property) {
        propertyElasticsearchRepository
                .save(PropertyDocument.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price(property.getPrice())
                .status(property.getStatus().toString())
                .location(property.getLocation())
                .city(property.getCity().getName())
                .ownerName(property.getOwner().getUsername())
                .build());

        log.info("Property added to elasticsearch");

    }

    public List<PropertyDocument> getPropertiesByCityName(String cityName) {
        return propertyElasticsearchRepository.findByCity(cityName);
    }

    public List<PropertyDocument> getPropertiesByPriceRange(double min, double max) {
        return propertyElasticsearchRepository.findByPriceBetween(min, max);
    }
}

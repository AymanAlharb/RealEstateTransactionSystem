package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.dto.request.PropertyMessage;
import com.ayman.realestatetransactionsystem.model.entity.PropertyDocument;
import com.ayman.realestatetransactionsystem.repository.PropertyElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SearchPropertyService {

    private final PropertyElasticsearchRepository propertyElasticsearchRepository;

    @RabbitListener(queues = {"${rabbitmq.property-queue.queue-name}"})
    public void createProperty(PropertyMessage property) {
        propertyElasticsearchRepository
                .save(PropertyDocument.builder()
                        .id(property.getId())
                        .title(property.getTitle())
                        .description(property.getDescription())
                        .price(property.getPrice())
                        .status(property.getStatus())
                        .location(property.getLocation())
                        .city(property.getCity())
                        .region(property.getRegion())
                        .ownerName(property.getOwnerName())
                        .build());

        log.info("Property {} added to elasticsearch", property.getTitle());

    }

    public List<PropertyDocument> getPropertiesByCityName(String cityName) {
        return propertyElasticsearchRepository.findByCity(cityName);
    }

    public List<PropertyDocument> getPropertiesByPriceRange(double min, double max) {
        return propertyElasticsearchRepository.findByPriceBetween(min, max);
    }
}

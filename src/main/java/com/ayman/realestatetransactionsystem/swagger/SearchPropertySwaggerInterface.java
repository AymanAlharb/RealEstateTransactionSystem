package com.ayman.realestatetransactionsystem.swagger;

import com.ayman.realestatetransactionsystem.model.entity.PropertyDocument;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SearchPropertySwaggerInterface {

    @GetMapping("/city/{cityName}")
    @Operation(
            summary = "Get properties by city name",
            description = "Elasticsearch public endpoint to fetch properties by city name."
    )
    List<PropertyDocument> getPropertiesByCityName(@PathVariable String cityName);

    @GetMapping("/price-range/{minPrice}/{maxPrice}")
    @Operation(
            summary = "Get properties by price range",
            description = "Elasticsearch public endpoint to fetch properties within a specified price range."
    )
    List<PropertyDocument> getPropertiesByPriceRange(@PathVariable double minPrice,
                                                     @PathVariable double maxPrice);
}

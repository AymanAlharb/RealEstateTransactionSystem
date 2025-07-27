package com.ayman.realestatetransactionsystem.controller;



import com.ayman.realestatetransactionsystem.model.PropertyDocument;
import com.ayman.realestatetransactionsystem.service.SearchPropertyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchPropertyController {

    private final SearchPropertyService searchPropertyService;


    @Operation(
            summary = "Adds a property",
            description = "Elasticsearch Public endpoint to fetch properties by cites"
    )
    @GetMapping("/get-by-city-name/{cityName}")
    public List<PropertyDocument> getPropertiesByCityName(@PathVariable String cityName){
        return searchPropertyService.getPropertiesByCityName(cityName);
    }

    @Operation(
            summary = "Adds a property",
            description = "Elasticsearch Public endpoint to fetch properties by price range"
    )
    @GetMapping("/get-by-price-range/{min}/{max}")
    public List<PropertyDocument> getPropertiesByCityName(@PathVariable double min, @PathVariable double max){
        return searchPropertyService.getPropertiesByPriceRange(min, max);
    }
}

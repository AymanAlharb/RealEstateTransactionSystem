package com.ayman.realestatetransactionsystem.controller;



import com.ayman.realestatetransactionsystem.constant.ApiRoutes;
import com.ayman.realestatetransactionsystem.model.entity.PropertyDocument;
import com.ayman.realestatetransactionsystem.service.SearchPropertyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.SEARCH_PROPERTY)
public class SearchPropertyController {

    private final SearchPropertyService searchPropertyService;

    @GetMapping("/city/{cityName}")
    public List<PropertyDocument> getPropertiesByCityName(@PathVariable String cityName){
        return searchPropertyService.getPropertiesByCityName(cityName);
    }

    @GetMapping("/price-range/{minPrice}/{maxPrice}")
    public List<PropertyDocument> getPropertiesByPriceRange(@PathVariable double minPrice, @PathVariable double maxPrice){
        return searchPropertyService.getPropertiesByPriceRange(minPrice, maxPrice);
    }
}

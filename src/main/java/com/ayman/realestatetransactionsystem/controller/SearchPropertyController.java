package com.ayman.realestatetransactionsystem.controller;



import com.ayman.realestatetransactionsystem.model.PropertyDocument;
import com.ayman.realestatetransactionsystem.service.SearchPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchPropertyController {

    private final SearchPropertyService searchPropertyService;


    @GetMapping("/get-by-city-name/{cityName}")
    public List<PropertyDocument> getPropertiesByCityName(@PathVariable String cityName){
        return searchPropertyService.getPropertiesByCityName(cityName);
    }

    @GetMapping("/get-by-price-range/{min}/{max}")
    public List<PropertyDocument> getPropertiesByCityName(@PathVariable double min, @PathVariable double max){
        return searchPropertyService.getPropertiesByCPriceRange(min, max);
    }
}

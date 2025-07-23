package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreateCityRequest;
import com.ayman.realestatetransactionsystem.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/city")
@RestController
public class CityController {
    private final CityService cityService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCity(@RequestBody @Valid CreateCityRequest cityRequest){
        cityService.addCity(cityRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("City added successfully"));
    }

}

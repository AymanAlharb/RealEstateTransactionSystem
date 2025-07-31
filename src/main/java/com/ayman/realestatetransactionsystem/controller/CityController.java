package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.constant.ApiRoutes;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateCityRequest;
import com.ayman.realestatetransactionsystem.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(ApiRoutes.CITY)
@RestController
public class CityController {
    private final CityService cityService;

    @Operation(
            summary = "Adds a city",
            description = "Only brokers can add cities",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "City name and region required for city creation"
            )
    )
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCity(@RequestBody @Valid CreateCityRequest cityRequest){
        cityService.addCity(cityRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("City added successfully"));
    }

}

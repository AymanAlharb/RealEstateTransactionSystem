package com.ayman.realestatetransactionsystem.swagger;


import com.ayman.realestatetransactionsystem.model.dto.request.CreateCityRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public interface CitySwaggerInterface {
    @PostMapping
    @Operation(
            summary = "Adds a city",
            description = "Only brokers can add cities",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "City name and region required for city creation"
            )
    )
    ResponseEntity<?> addCity(CreateCityRequest cityRequest);
}

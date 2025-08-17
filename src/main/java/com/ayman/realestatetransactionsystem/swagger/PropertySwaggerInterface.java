package com.ayman.realestatetransactionsystem.swagger;

import com.ayman.realestatetransactionsystem.model.dto.request.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateUpdatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

public interface PropertySwaggerInterface {

    @PostMapping
    @Operation(
            summary = "Add a property",
            description = "Only brokers can add properties. " +
                    "Properties can only be added to sellers.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property details required for property creation"
            )
    )
    ResponseEntity<ApiResponse> addProperty(@Valid @RequestBody CreatePropertyRequest propertyRequest);

    @PutMapping("/{propertyId}")
    @Operation(
            summary = "Update a property",
            description = "Only sellers who own the property can update it.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property details required for property update"
            )
    )
    ResponseEntity<ApiResponse> updateProperty(@PathVariable Long propertyId,
                                               @Valid @RequestBody CreateUpdatePropertyRequest updatePropertyRequest);

    @DeleteMapping("/{propertyId}")
    @Operation(
            summary = "Delete a property",
            description = "Only sellers who own the property can delete it."
    )
    ResponseEntity<ApiResponse> deleteProperty(@PathVariable Long propertyId);
}

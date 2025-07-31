package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.constant.ApiRoutes;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.request.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateUpdatePropertyRequest;
import com.ayman.realestatetransactionsystem.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(ApiRoutes.PROPERTY)
@RestController
public class PropertyController {
    private final PropertyService propertyService;

    @Operation(
            summary = "Adds a property",
            description = "Only brokers can add properties" +
                    "Properties can only be added to sellers",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property details required for property creation"
            )
    )
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProperty(@Valid @RequestBody CreatePropertyRequest propertyRequest){
        propertyService.addProperty(propertyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property added successfully."));
    }

    @Operation(
            summary = "Adds a property",
            description = "Only sellers who own the property can update it",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property details required for property update"
            )
    )
    @PutMapping("/update/{propertyId}")
    public ResponseEntity<ApiResponse> updateProperty(@PathVariable Long propertyId, @RequestBody @Valid CreateUpdatePropertyRequest updatePropertyRequest){
        propertyService.updateProperty(propertyId, updatePropertyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property updated successfully."));
    }

    @Operation(
            summary = "Adds a property",
            description = "Only sellers who own the property can delete it",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property details required for property delete"
            )
    )
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<ApiResponse> deleteProperty(@PathVariable Long propertyId){
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property deleted successfully."));
    }
}

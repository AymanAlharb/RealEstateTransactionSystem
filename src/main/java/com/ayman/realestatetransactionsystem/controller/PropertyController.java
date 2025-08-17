package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.constant.ApiRoutes;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.request.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateUpdatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.response.PropertyCreatedResponse;
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

    @PostMapping
    public ResponseEntity<PropertyCreatedResponse> addProperty(@Valid @RequestBody CreatePropertyRequest propertyRequest){
        return ResponseEntity.status(HttpStatus.OK).body(propertyService.addProperty(propertyRequest));
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<ApiResponse> updateProperty(@PathVariable Long propertyId, @RequestBody @Valid CreateUpdatePropertyRequest updatePropertyRequest){
        propertyService.updateProperty(propertyId, updatePropertyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property with the title: " + updatePropertyRequest.getTitle() + " updated successfully."));
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<ApiResponse> deleteProperty(@PathVariable Long propertyId){
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property with the id: " + propertyId + " deleted successfully."));
    }
}

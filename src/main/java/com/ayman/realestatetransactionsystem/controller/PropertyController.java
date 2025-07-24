package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.model.dto.CreateUpdatePropertyRequest;
import com.ayman.realestatetransactionsystem.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/property")
@RestController
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProperty(@Valid @RequestBody CreatePropertyRequest propertyRequest){
        propertyService.addProperty(propertyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property added successfully."));
    }

    @PutMapping("/update/{propertyId}")
    public ResponseEntity<ApiResponse> updateProperty(@PathVariable Long propertyId, @RequestBody @Valid CreateUpdatePropertyRequest updatePropertyRequest){
        propertyService.updateProperty(propertyId, updatePropertyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property updated successfully."));
    }

    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<ApiResponse> deleteProperty(@PathVariable Long propertyId){
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Property deleted successfully."));
    }
}

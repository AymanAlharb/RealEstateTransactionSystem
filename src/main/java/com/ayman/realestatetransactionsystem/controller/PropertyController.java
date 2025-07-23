package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreatePropertyRequest;
import com.ayman.realestatetransactionsystem.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

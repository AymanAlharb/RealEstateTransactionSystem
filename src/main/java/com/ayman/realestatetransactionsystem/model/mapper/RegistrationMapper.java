package com.ayman.realestatetransactionsystem.model.mapper;

import com.ayman.realestatetransactionsystem.model.dto.response.RegistrationResponse;
import com.ayman.realestatetransactionsystem.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;


public class RegistrationMapper {
    public static RegistrationResponse createRegistrationResponse(User user){
        return RegistrationResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

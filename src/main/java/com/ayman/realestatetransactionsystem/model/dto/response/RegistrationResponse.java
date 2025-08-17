package com.ayman.realestatetransactionsystem.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RegistrationResponse {
    private String username;
    private String email;
    private LocalDateTime createdAt;
}

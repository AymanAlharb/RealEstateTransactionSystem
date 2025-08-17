package com.ayman.realestatetransactionsystem.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiResponse {
    private LocalDateTime timeStamp = LocalDateTime.now();
    private HttpStatus status = HttpStatus.OK;
    private String message;
    public ApiResponse (String message){
        this.message = message;
    }
    public ApiResponse (String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }
}

package com.ayman.realestatetransactionsystem.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }

}

package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.service.EmailSenderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Slf4j
@RestController
public class NotificationController {
    private final EmailSenderService emailSenderService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendEmail(@RequestBody String body) throws MessagingException, IOException {
        log.info("Hello, I have been called from this thread: {}", Thread.currentThread().getName());
        emailSenderService.sendEmail("n18ayman@gmail.com", "Testing", body);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success"));
    }

}

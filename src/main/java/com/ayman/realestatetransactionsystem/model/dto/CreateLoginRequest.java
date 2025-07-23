package com.ayman.realestatetransactionsystem.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoginRequest {
    private String username;
    private String password;
}

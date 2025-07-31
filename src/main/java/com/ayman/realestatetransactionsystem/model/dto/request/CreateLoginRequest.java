package com.ayman.realestatetransactionsystem.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}

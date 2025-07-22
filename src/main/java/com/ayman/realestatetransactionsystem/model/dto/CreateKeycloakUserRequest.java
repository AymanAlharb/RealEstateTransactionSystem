package com.ayman.realestatetransactionsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateKeycloakUserRequest {
    private String username;
    private boolean enabled;
    private List<Credentials> credentials;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Credentials {
        private String type;
        private String value;
        private boolean temporary;
    }
}

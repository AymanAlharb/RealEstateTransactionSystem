package com.ayman.realestatetransactionsystem.model.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

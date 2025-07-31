package com.ayman.realestatetransactionsystem.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Validated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakProperties {

    private Urls urls;
    private Client client;
    private String resourceId;
    private Jwt jwt;

    @Getter @Setter
    public static class Urls {
        private String addUser;
        private String getUser;
        private String getClientId;
        private String getToken;
        private String getRoleId;
        private String assignRole;
    }

    @Getter @Setter
    public static class Client {
        private String id;
        private String secret;
    }

    @Getter @Setter
    public static class Jwt {
        private String principleAttribute;
    }
}

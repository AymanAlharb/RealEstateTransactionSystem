package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.exception.ApiException;
import com.ayman.realestatetransactionsystem.model.User;
import com.ayman.realestatetransactionsystem.model.dto.CreateAssignRoleRequest;
import com.ayman.realestatetransactionsystem.model.dto.CreateKeycloakUserRequest;
import com.ayman.realestatetransactionsystem.model.dto.CreateLoginRequest;
import com.ayman.realestatetransactionsystem.model.dto.CreateUserRequest;
import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    @Value("${add-user-url}")
    String addUserUrl;
    @Value("${get-user-keycloak-url}")
    String getUserKeycloakUrl;
    @Value("${get-client-id-url}")
    String getClientIdUrl;
    @Value("${get-user-token-url}")
    String getUserTokenUrl;
    @Value("${resource-id}")
    String resourceId;
    @Value("${get-role-id-url}")
    String getRoleIdUrl;
    @Value("${assign-role-keycloak-url}")
    String assignRoleUrl;
    @Value("${admin-client-id}")
    String client_id;
    @Value("${client-secret}")
    String client_secret;
    String adminToken = "";
    private final UserRepository userRepository;
    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

    public void registerUser(CreateUserRequest userRequest) {

        validateUsernameAndEmail(userRequest);
        UserRoleEnum userRoleEnum = assignRoleEnum(userRequest.getRole().toUpperCase());

        // Create the user.
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(userRoleEnum)
                .build();

        // Add User to Keycloak realm.
        addUserToKeycloak(user);

        // Save to the database.
        userRepository.save(user);
        log.info("New {} with the username {} signup", user.getRole(), user.getUsername());
    }

    public String login(CreateLoginRequest loginRequest) {
        // Build URL Encode
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", resourceId);
        formData.add("grant_type", "password");
        formData.add("password", loginRequest.getPassword());
        formData.add("username", loginRequest.getUsername());
        return getUserToken(formData);
    }

    private UserRoleEnum assignRoleEnum(String role) {
        switch (role) {
            case "BUYER" -> {
                return UserRoleEnum.BUYER;
            }
            case "SELLER" -> {
                return UserRoleEnum.SELLER;
            }
            default -> {
                return UserRoleEnum.BROKER;
            }
        }
    }

    private void validateUsernameAndEmail(CreateUserRequest userRequest) {
        // Check if the email unique
        if (userRepository.findUserByEmail(userRequest.getEmail()) != null) {
            log.warn("User with the username {} tried to sign up with a used email: {}",
                    userRequest.getUsername(), userRequest.getEmail());
            throw new ApiException("The email is used");
        }

        // Check if the username unique
        if (userRepository.findUserByUsername(userRequest.getUsername()) != null) {
            log.warn("User with the email {} tried to sign up with a used username: {}",
                    userRequest.getEmail(), userRequest.getUsername());
            throw new ApiException("The username is used");
        }
    }

    private void addUserToKeycloak(User user) {
        // Get Admin token
        if (Objects.equals(adminToken, "")) {
            log.info("admin token null");
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", client_id);
            formData.add("client_secret", client_secret);
            formData.add("grant_type", "client_credentials");
            adminToken = getUserToken(formData);
            log.info(adminToken);
        }
        // Add user to realm
        addUserToRealm(user, adminToken);
        // Get user keycloak id
        String userKeycloakId = getUserKeycloakId(user.getUsername());
        // Get client id
        String clientId = getClientId();
        // Get role id
        String roleId = getRoleId(clientId, user.getRole().toString());
        // Assign role
        assignRoleOnKeycloak(userKeycloakId, clientId, roleId, user.getRole().toString().toUpperCase());

    }

    private String getUserToken(MultiValueMap<String, String> formData) {

        String response = webClient.post()
                .uri(getUserTokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root;
        try {
            root = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return root.get("access_token").asText();
    }

    private void addUserToRealm(User user, String adminToken) {
        boolean enabledAccount = true;
        String type = "password";
        boolean temporaryPassword = false;
        String value = user.getPassword();
        List<CreateKeycloakUserRequest.Credentials> credentials = new ArrayList<>();
        CreateKeycloakUserRequest.Credentials credential = new CreateKeycloakUserRequest.Credentials(type, value, temporaryPassword);
        credentials.add(0, credential);
        CreateKeycloakUserRequest request = new CreateKeycloakUserRequest(user.getUsername(), enabledAccount, credentials);

        webClient.post()
                .uri(addUserUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(Mono.just(request), CreateKeycloakUserRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("User with the username: {} added to keycloak realm successfully", user.getUsername());

    }

    private String getUserKeycloakId(String username) {
        String response = webClient.get()
                .uri(getUserKeycloakUrl + username)
                .header("Authorization", "Bearer " + adminToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root;
        try {
            root = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode userNode = root.get(0);
        return userNode.get("id").asText();
    }

    private String getClientId() {
        String response = webClient.get()
                .uri(getClientIdUrl)
                .header("Authorization", "Bearer " + adminToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional<JsonNode> clientNode = StreamSupport.stream(root.spliterator(), false)
                .filter(client -> resourceId.equals(client.get("clientId").asText()))
                .findFirst();

        return clientNode
                .map(client -> client.get("id").asText())
                .orElseThrow(() -> new RuntimeException("client with client id: '" + resourceId + "' not found."));
    }

    private String getRoleId(String clientId, String role) {
        String response = webClient.get()
                .uri(getRoleIdUrl + clientId + "/roles/" + role.toUpperCase())
                .header("Authorization", "Bearer " + adminToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root;
        try {
            root = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return root.get("id").asText();
    }

    private void assignRoleOnKeycloak(String userId, String clientId, String roleId, String roleName) {
        CreateAssignRoleRequest request = new CreateAssignRoleRequest(roleId, roleName);
        webClient.post()
                .uri(assignRoleUrl + userId + "/role-mappings/clients/" + clientId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(Mono.just(List.of(request)), new ParameterizedTypeReference<>() {
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}

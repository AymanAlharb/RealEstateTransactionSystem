package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.dto.response.RegistrationResponse;
import com.ayman.realestatetransactionsystem.properties.KeycloakProperties;
import com.ayman.realestatetransactionsystem.model.entity.User;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateAssignRoleRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateKeycloakUserRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateLoginRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateUserRequest;
import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.ayman.realestatetransactionsystem.model.mapper.RegistrationMapper.createRegistrationResponse;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final KeycloakProperties keycloakProperties;
    private final UserRepository userRepository;
    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

    @Transactional
    public RegistrationResponse registerUser(CreateUserRequest userRequest) {

        checkDataUniqueness(userRequest);

        // Create the user.
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(new BCryptPasswordEncoder().encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(UserRoleEnum.getCode(userRequest.getRole()))
                .build();

        // Add User to Keycloak realm.
        addUserToKeycloak(user);

        // Save to the database.
        userRepository.save(user);
        log.info("New user: {} with the username: {} signed-up", user.getRole(), user.getUsername());

        return createRegistrationResponse(user);
    }

    public String login(CreateLoginRequest loginRequest) {
        // Validate login info
        User user = userRepository.findUserByUsername(loginRequest.getUsername());
        if (user == null) throw new BadCredentialsException("Wrong username or password");

        if (!(new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword()))) {
            throw new BadCredentialsException("Wrong username or password");
        }

        // Build URL Encode
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.getResourceId());
        formData.add("grant_type", "password");
        formData.add("password", user.getPassword());
        formData.add("username", loginRequest.getUsername());
        return getUserToken(formData, loginRequest.getUsername());
    }

    private void checkDataUniqueness(CreateUserRequest userRequest) {
        // Check if the email unique
        if (userRepository.findUserByEmail(userRequest.getEmail()) != null) {
            log.warn("User with the username: {} tried to sign up with a used email: {}",
                    userRequest.getUsername(), userRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        // Check if the username unique
        if (userRepository.findUserByUsername(userRequest.getUsername()) != null) {
            log.warn("User with the email: {} tried to sign up with a used username: {}",
                    userRequest.getEmail(), userRequest.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }

        // Check if the phone number is unique
        if (userRepository.findUserByPhoneNumber(userRequest.getPhoneNumber()) != null) {
            log.warn("User with the email: {} tried to sign up with a used phone number: {}",
                    userRequest.getEmail(), userRequest.getPhoneNumber());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is already in use");
        }
    }

    private void addUserToKeycloak(User user) {
        // Get Admin token
        String adminToken = getAdminToken();

        // Add user to realm
        addUserToRealm(user, adminToken);

        // Get user keycloak id
        String userKeycloakId = getUserKeycloakId(user.getUsername(), adminToken);

        // Get client id
        String clientId = getClientId(adminToken);

        // Get role id
        String roleId = getRoleId(clientId, user.getRole().toString(), adminToken);

        // Assign role
        assignRoleOnKeycloak(userKeycloakId, clientId, roleId, user.getRole().toString().toUpperCase(), user.getUsername(), adminToken);

    }

    private String getAdminToken() {
        // Build URL Encode
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.getClient().getId());
        formData.add("client_secret", keycloakProperties.getClient().getSecret());
        formData.add("grant_type", "client_credentials");

        return getUserToken(formData, "admin");
    }

    private String getUserToken(MultiValueMap<String, String> formData, String username) {

        String response = webClient.post()
                .uri(keycloakProperties.getUrls().getGetToken())
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
        log.info("{} logged in", username);
        return root.get("access_token").asText();
    }

    private void addUserToRealm(User user, String adminToken) {
        CreateKeycloakUserRequest request = getCreateKeycloakUserRequest(user);

        webClient.post()
                .uri(keycloakProperties.getUrls().getAddUser())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(Mono.just(request), CreateKeycloakUserRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("User with the username: {} added to keycloak realm successfully", user.getUsername());

    }

    private static CreateKeycloakUserRequest getCreateKeycloakUserRequest(User user) {
        boolean enabledAccount = true;
        String type = "password";
        boolean temporaryPassword = false;
        String value = user.getPassword();
        List<CreateKeycloakUserRequest.Credentials> credentials = new ArrayList<>();
        CreateKeycloakUserRequest.Credentials credential = new CreateKeycloakUserRequest.Credentials(type, value, temporaryPassword);
        credentials.add(0, credential);
        return new CreateKeycloakUserRequest(user.getUsername(), enabledAccount, credentials);
    }

    private String getUserKeycloakId(String username, String adminToken) {
        String response = webClient.get()
                .uri(keycloakProperties.getUrls().getGetUser() + username)
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
        log.info("User Keycloak id for the user: {} obtained from keycloak", username);
        return userNode.get("id").asText();
    }

    private String getClientId(String adminToken) {
        String response = webClient.get()
                .uri(keycloakProperties.getUrls().getGetClientId())
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
                .filter(client -> keycloakProperties.getResourceId().equals(client.get("clientId").asText()))
                .findFirst();

        return clientNode
                .map(client -> client.get("id").asText())
                .orElseThrow(() -> new RuntimeException("client with client id: '" + keycloakProperties.getResourceId() + "' not found."));
    }

    private String getRoleId(String clientId, String role, String adminToken) {
        String response = webClient.get()
                .uri(keycloakProperties.getUrls().getGetRoleId() + clientId + "/roles/" + role.toUpperCase())
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

    private void assignRoleOnKeycloak(String userId, String clientId, String roleId, String roleName, String username, String adminToken) {
        CreateAssignRoleRequest request = new CreateAssignRoleRequest(roleId, roleName);

        webClient.post()
                .uri(keycloakProperties.getUrls().getAssignRole() + userId + "/role-mappings/clients/" + clientId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(Mono.just(List.of(request)), new ParameterizedTypeReference<>() {
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("User: {} assigned with the role: {} to Keycloak", username, roleName);
    }
}

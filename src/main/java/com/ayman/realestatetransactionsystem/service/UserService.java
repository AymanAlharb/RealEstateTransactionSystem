package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.exception.ApiException;
import com.ayman.realestatetransactionsystem.model.User;
import com.ayman.realestatetransactionsystem.model.dto.CreateUserRequest;
import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

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

        userRepository.save(user);
        log.info("New {} with the username {} signup", user.getRole(), user.getUsername());
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
}

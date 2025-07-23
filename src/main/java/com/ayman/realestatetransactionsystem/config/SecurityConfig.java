package com.ayman.realestatetransactionsystem.config;

import com.ayman.realestatetransactionsystem.service.JwtAuthConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverterService jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/user/register",
                        "/api/v1/user/login",
                        "/api/v1/city/add")
                .permitAll()
                .requestMatchers("/api/v1/property/add",
                        "/api/v1/transection/broker-process-transection")
                .hasRole("BROKER")
                .requestMatchers("/api/v1/transection/request/")
                .hasRole("BUYER")
                .requestMatchers("/api/v1/transection/seller-process-transection")
                .hasRole("SELLER")
                .requestMatchers("/api/v1/account/add")
                .hasAnyRole("BROKER", "SELLER", "BUYER")
                .anyRequest()
                .authenticated();

        http
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);

        http
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);

        return http.build();
    }
}

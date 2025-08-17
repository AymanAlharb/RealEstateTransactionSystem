package com.ayman.realestatetransactionsystem.config;

import com.ayman.realestatetransactionsystem.service.JwtAuthConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui/index.html")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth", "/api/v1/auth/token")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/search/city/*", "/api/v1/search/price-range/*/*")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/properties", "/api/v1/cities")
                .hasRole("BROKER")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/transactions/broker-process")
                .hasRole("BROKER")
                .requestMatchers(HttpMethod.POST, "/api/v1/transactions/*/request")
                .hasRole("BUYER")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/transactions/seller-process")
                .hasRole("SELLER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/properties/*")
                .hasRole("SELLER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/properties/*")
                .hasRole("SELLER")
                .requestMatchers(HttpMethod.POST, "/api/v1/accounts")
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

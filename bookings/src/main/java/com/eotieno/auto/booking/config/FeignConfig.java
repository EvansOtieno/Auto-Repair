package com.eotieno.auto.booking.config;


import com.eotieno.auto.booking.dto.AuthRequest;
import com.eotieno.auto.booking.dto.AuthResponse;
import com.eotieno.auto.booking.service.UserServiceClient;
import feign.RequestInterceptor;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class FeignConfig {
    @Autowired
    UserServiceClient userService;
    private String cachedToken;
    private Instant tokenExpiry;

    @Value("${book.service.email}")
    private String serviceEmail;
    @Value("${book.service.password}")
    private String servicePassword;


    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            if (cachedToken == null || Instant.now().isAfter(tokenExpiry)) {
                refreshToken();
            }
            requestTemplate.header("Authorization", "Bearer " + cachedToken);
        };
    }

    @Synchronized
    private void refreshToken() {
        AuthRequest request = AuthRequest.builder()
                .identifier(serviceEmail)
                .password(servicePassword)
                .build();

        AuthResponse response = userService.authenticate(request);
        this.cachedToken = response.getToken();
        this.tokenExpiry = response.getExpiry().minusSeconds(60); // Refresh 1min before expiry
    }

//    @Value("${service.api.key}")
//    private String apiKey;
//
//    @Bean
//    public RequestInterceptor apiKeyInterceptor() {
//        return requestTemplate -> {
//            requestTemplate.header("X-API-KEY", apiKey);
//        };
//    }

}
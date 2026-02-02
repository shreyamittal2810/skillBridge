package com.skillbridge.communication_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentClient {

    private final RestTemplate restTemplate;

    @Value("${services.user-service.url}")
    private String userServiceUrl;

    public boolean studentExists(Long studentId) {
        try {
            restTemplate.getForEntity(
                userServiceUrl + "/students/" + studentId + "/exists",
                Void.class
            );
            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }
}

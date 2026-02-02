package com.skillbridge.communication_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectClient {

    private final RestTemplate restTemplate;

    @Value("${services.project-service.url}")
    private String projectServiceUrl;

    public boolean isMember(Long projectId, Long studentId) {
        try {
            restTemplate.getForEntity(
                projectServiceUrl + "/projects/" + projectId + "/members/" + studentId,
                Void.class
            );
            return true;
        } catch (HttpClientErrorException.Forbidden |
                 HttpClientErrorException.NotFound ex) {
            return false;
        }
    }
}

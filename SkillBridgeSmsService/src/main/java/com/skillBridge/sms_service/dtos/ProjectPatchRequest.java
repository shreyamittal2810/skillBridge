package com.skillBridge.sms_service.dtos;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectPatchRequest {

    // Optional fields for partial update

    private String title;

    private String description;

    private Set<String> requiredSkills;
}

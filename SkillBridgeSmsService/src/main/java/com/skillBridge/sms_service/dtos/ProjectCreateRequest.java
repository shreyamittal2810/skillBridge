package com.skillBridge.sms_service.dtos;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotEmpty
    private Set<String> requiredSkills;


}

package com.skillBridge.sms_service.dtos;

import com.skillBridge.sms_service.entities.ProjectStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectStatusUpdateRequest {

    @NotNull
    private ProjectStatus status;
}

package com.skillBridge.sms_service.dtos;

import com.skillBridge.sms_service.entities.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusUpdateRequest {

    @NotNull
    private ApplicationStatus status;
}

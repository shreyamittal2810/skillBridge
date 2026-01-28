package com.skillBridge.sms_service.dtos;

import java.time.LocalDateTime;

import com.skillBridge.sms_service.entities.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationResponse {

    private Long applicationId;
    private Long projectId;
    private Long senderId;
    private String messageText;
    private ApplicationStatus applicationStatus;
    private LocalDateTime createdOn;
}

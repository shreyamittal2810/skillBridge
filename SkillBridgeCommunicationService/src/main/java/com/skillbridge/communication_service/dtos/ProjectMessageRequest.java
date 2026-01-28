package com.skillbridge.communication_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMessageRequest {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Sender student ID is required")
    private Long senderStudentId;

    @NotBlank(message = "Message cannot be empty")
    private String message;
}

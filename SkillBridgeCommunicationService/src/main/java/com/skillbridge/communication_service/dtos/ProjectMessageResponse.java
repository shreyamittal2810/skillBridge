package com.skillbridge.communication_service.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMessageResponse {

    private Long id;
    private Long projectId;
    private Long senderStudentId;
    private String message;
    private LocalDateTime createdAt;
}

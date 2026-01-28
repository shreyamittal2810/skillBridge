package com.skillbridge.communication_service.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectMessageResponse {

    private Long id;
    private Long senderStudentId;
    private Long receiverStudentId;
    private String message;
    private LocalDateTime createdAt;
}

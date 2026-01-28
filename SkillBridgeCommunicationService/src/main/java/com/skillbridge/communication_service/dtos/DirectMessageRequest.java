package com.skillbridge.communication_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectMessageRequest {

    @NotNull(message = "Sender student ID is required")
    private Long senderStudentId;

    @NotNull(message = "Receiver student ID is required")
    private Long receiverStudentId;

    @NotBlank(message = "Message cannot be empty")
    private String message;
}

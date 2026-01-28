package com.skillbridge.communication_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectMessageUpdateRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;
}

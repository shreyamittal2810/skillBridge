package com.skillBridge.sms_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentAuthResponse {

    private Long studentId;
    private String email;
    private String token;
}

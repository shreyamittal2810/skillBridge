package com.skillBridge.sms_service.dtos;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentResponse {

    private Long studentId;
    private String name;
    private String email;
    private String course;
    private Set<String> skills;
}

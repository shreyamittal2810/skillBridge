package com.skillBridge.sms_service.dtos;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentUpdateRequest {

    private String name;
    private String course;
    private Set<String> skills;
}

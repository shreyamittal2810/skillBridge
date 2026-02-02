package com.skillBridge.sms_service.dtos;

import java.util.Set;

import com.skillBridge.sms_service.entities.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {

    private Long projectId;
    private String title;
    private String description;
    private ProjectStatus status;
    private Set<String> requiredSkills;
    private Long createdBy;
    private Set<Long> teamMembers;
}

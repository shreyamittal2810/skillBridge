package com.skillBridge.sms_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/projects")
@RequiredArgsConstructor
public class ProjectInternalController {

    private final ProjectService projectService;

    @GetMapping("/{projectId}/members/{studentId}/exists")
    public boolean isMember(
            @PathVariable Long projectId,
            @PathVariable Long studentId
    ) {
        return projectService.isMember(projectId, studentId);
    }
}

package com.skillBridge.sms_service.service;

import java.util.List;

import com.skillBridge.sms_service.dtos.ProjectCreateRequest;
import com.skillBridge.sms_service.dtos.ProjectPatchRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.entities.ProjectStatus;

public interface ProjectService {

    // CREATE
    ProjectResponse create(ProjectCreateRequest request, Long loggedInStudentId);

    // READ
    ProjectResponse get(Long id);
    List<ProjectResponse> getAll();

    // UPDATE
    ProjectResponse update(Long id, ProjectCreateRequest request);
    ProjectResponse patch(Long id, ProjectPatchRequest request);
    ProjectResponse updateStatus(Long id, ProjectStatus status);

    // DELETE
    void delete(Long id);
}

package com.skillBridge.sms_service.service;

import java.util.List;

import com.skillBridge.sms_service.dtos.ModifyProjectMembersRequest;
import com.skillBridge.sms_service.dtos.ProjectCreateRequest;
import com.skillBridge.sms_service.dtos.ProjectPatchRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.entities.ProjectStatus;

public interface ProjectService {

    // ================= CREATE =================
    ProjectResponse create(ProjectCreateRequest request, Long studentId);

    // ================= READ (PUBLIC) =================
    ProjectResponse get(Long id);
    List<ProjectResponse> getAll();

    // ================= UPDATE =================
    ProjectResponse update(
            Long id,
            ProjectCreateRequest request,
            Long studentId
    );

    ProjectResponse patch(
            Long id,
            ProjectPatchRequest request,
            Long studentId
    );

    ProjectResponse updateStatus(
            Long id,
            ProjectStatus status,
            Long studentId
    );

    // ================= DELETE =================
    void delete(Long id, Long studentId);
    
    ProjectResponse modifyProjectMembers(
            Long projectId,
            ModifyProjectMembersRequest request,
            Long requesterId
    );

	boolean isMember(Long projectId, Long studentId);

}


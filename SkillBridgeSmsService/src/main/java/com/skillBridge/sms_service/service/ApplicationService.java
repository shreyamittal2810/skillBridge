package com.skillBridge.sms_service.service;

import java.util.List;

import com.skillBridge.sms_service.dtos.ApplicationRequest;
import com.skillBridge.sms_service.dtos.ApplicationResponse;
import com.skillBridge.sms_service.dtos.ApplicationStatusUpdateRequest;
import com.skillBridge.sms_service.dtos.ApplicationUpdateRequest;

public interface ApplicationService {

    // ================= APPLY TO PROJECT =================
    ApplicationResponse create(ApplicationRequest request, Long studentId);

    // ================= READ =================
    ApplicationResponse get(Long applicationId, Long studentId);
    List<ApplicationResponse> getAll(Long studentId);

    // ================= UPDATE =================
    ApplicationResponse updateMessage(
            Long applicationId,
            ApplicationUpdateRequest request,
            Long studentId
    );

    // Only project owner can update status
    ApplicationResponse updateStatus(
            Long applicationId,
            ApplicationStatusUpdateRequest request,
            Long studentId
    );

    // ================= DELETE =================
    void delete(Long applicationId, Long studentId);

 // Creator/Admin: view applications for a project
    List<ApplicationResponse> getApplicationsForProject(Long projectId, Long requesterId);
}

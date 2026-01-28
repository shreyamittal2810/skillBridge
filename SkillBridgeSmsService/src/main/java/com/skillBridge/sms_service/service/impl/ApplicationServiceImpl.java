package com.skillBridge.sms_service.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillBridge.sms_service.dtos.ApplicationRequest;
import com.skillBridge.sms_service.dtos.ApplicationResponse;
import com.skillBridge.sms_service.dtos.ApplicationStatusUpdateRequest;
import com.skillBridge.sms_service.dtos.ApplicationUpdateRequest;
import com.skillBridge.sms_service.entities.Application;
import com.skillBridge.sms_service.repository.ApplicationRepository;
import com.skillBridge.sms_service.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    // ================= APPLY TO PROJECT =================
    @Override
    public ApplicationResponse create(ApplicationRequest request, Long studentId) {

        boolean exists = applicationRepository
                .existsByProjectIdAndSenderId(
                        request.getProjectId(),
                        studentId
                );

        if (exists) {
            throw new IllegalStateException("You have already applied to this project");
        }

        Application app = new Application();
        app.setProjectId(request.getProjectId());
        app.setSenderId(studentId); // 🔐 from JWT
        app.setMessageText(request.getMessageText());

        return mapToResponse(applicationRepository.save(app));
    }

    // ================= READ ONE =================
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse get(Long applicationId, Long studentId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        // Applicant can view
        if (!app.getSenderId().equals(studentId)) {
            throw new SecurityException("Not authorized to view this application");
        }

        return mapToResponse(app);
    }

    // ================= READ MY APPLICATIONS =================
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAll(Long studentId) {

        return applicationRepository.findBySenderId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE MESSAGE (APPLICANT ONLY) =================
    @Override
    public ApplicationResponse updateMessage(
            Long applicationId,
            ApplicationUpdateRequest request,
            Long studentId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        if (!app.getSenderId().equals(studentId)) {
            throw new SecurityException("You can update only your own application");
        }

        app.setMessageText(request.getMessageText());

        return mapToResponse(applicationRepository.save(app));
    }

    // ================= UPDATE STATUS (PROJECT OWNER – LOGIC READY) =================
    @Override
    public ApplicationResponse updateStatus(
            Long applicationId,
            ApplicationStatusUpdateRequest request,
            Long studentId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        /*
         * NOTE (for next step):
         * Here you should check:
         * project.createdBy == studentId
         * using ProjectRepository
         */

        app.setApplicationStatus(request.getStatus());
        return mapToResponse(applicationRepository.save(app));
    }

    // ================= DELETE (APPLICANT ONLY) =================
    @Override
    public void delete(Long applicationId, Long studentId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        if (!app.getSenderId().equals(studentId)) {
            throw new SecurityException("You can delete only your own application");
        }

        applicationRepository.delete(app);
    }

    // ================= MAPPER =================
    private ApplicationResponse mapToResponse(Application app) {
        return new ApplicationResponse(
                app.getApplicationId(),
                app.getProjectId(),
                app.getSenderId(),
                app.getMessageText(),
                app.getApplicationStatus(),
                app.getCreatedOn()
        );
    }
}

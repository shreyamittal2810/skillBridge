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
import com.skillBridge.sms_service.entities.Project;
import com.skillBridge.sms_service.entities.Role;
import com.skillBridge.sms_service.entities.Student;
import com.skillBridge.sms_service.repository.ApplicationRepository;
import com.skillBridge.sms_service.repository.ProjectRepository;
import com.skillBridge.sms_service.repository.StudentRepository;
import com.skillBridge.sms_service.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    // ================= APPLY TO PROJECT =================
    @Override
    public ApplicationResponse create(ApplicationRequest request, Long studentId) {

        boolean exists = applicationRepository
                .existsByProjectIdAndStudentId(
                        request.getProjectId(),
                        studentId
                );

        if (exists) {
            throw new IllegalStateException("You have already applied to this project");
        }

        Application app = new Application();
        app.setProjectId(request.getProjectId());
        app.setStudentId(studentId); // 🔐 from JWT
        app.setMessage(request.getMessageText());

        return mapToResponse(applicationRepository.save(app));
    }

    // ================= READ ONE =================
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse get(Long applicationId, Long studentId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        Student requester = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        Project project = projectRepository.findById(app.getProjectId())
                .orElseThrow(() -> new IllegalStateException("Project not found"));

        boolean isSender = app.getStudentId().equals(studentId);
        boolean isCreator = project.getCreatedBy().equals(studentId);
        boolean isAdmin = requester.getRole() == Role.ADMIN;

        if (!isSender && !isCreator && !isAdmin) {
            throw new SecurityException("Not authorized to view this application");
        }

        return mapToResponse(app);
    }


    // ================= READ MY APPLICATIONS =================
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAll(Long studentId) {

        return applicationRepository.findByStudentId(studentId)
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

        if (!app.getStudentId().equals(studentId)) {
            throw new SecurityException("You can update only your own application");
        }

        app.setMessage(request.getMessageText());

        return mapToResponse(applicationRepository.save(app));
    }

    // ================= UPDATE STATUS (PROJECT OWNER – LOGIC READY) =================
    @Override
    public ApplicationResponse updateStatus(
            Long applicationId,
            ApplicationStatusUpdateRequest request,
            Long studentId
    ) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        Project project = projectRepository.findById(app.getProjectId())
                .orElseThrow(() -> new IllegalStateException("Project not found"));

        Student requester = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isCreator = project.getCreatedBy().equals(studentId);

        if (!isAdmin && !isCreator) {
            throw new SecurityException(
                    "Only admin or project creator can update application status"
            );
        }

        app.setStatus(request.getStatus());
        return mapToResponse(applicationRepository.save(app));
    }


    // ================= DELETE (APPLICANT ONLY) =================
    @Override
    public void delete(Long applicationId, Long studentId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        if (!app.getStudentId().equals(studentId)) {
            throw new SecurityException("You can delete only your own application");
        }

        applicationRepository.delete(app);
    }

    // ================= MAPPER =================
    private ApplicationResponse mapToResponse(Application app) {
        return new ApplicationResponse(
                app.getApplicationId(),
                app.getProjectId(),
                app.getStudentId(),
                app.getMessage(),
                app.getStatus(),
                app.getCreatedOn()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsForProject(
            Long projectId,
            Long requesterId
    ) {
        // 1️⃣ Fetch project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new IllegalStateException("Project not found"));

        // 2️⃣ Fetch requester
        Student requester = studentRepository.findById(requesterId)
                .orElseThrow(() ->
                        new IllegalStateException("Student not found"));

        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isCreator = project.getCreatedBy().equals(requesterId);

        // 3️⃣ Permission check
        if (!isAdmin && !isCreator) {
            throw new SecurityException(
                    "You are not allowed to view applications for this project"
            );
        }

        // 4️⃣ Fetch & map applications
        return applicationRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

}

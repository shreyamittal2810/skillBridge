package com.skillBridge.sms_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.ProjectCreateRequest;
import com.skillBridge.sms_service.dtos.ProjectPatchRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.dtos.ProjectStatusUpdateRequest;
import com.skillBridge.sms_service.security.UserPrincipal;
import com.skillBridge.sms_service.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long loggedInStudentId = userPrincipal.getStudentId();

        return ResponseEntity.ok(
                projectService.create(request, loggedInStudentId)
        );
    }

    // ================= READ ONE =================
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.get(id));
    }

    // ================= READ ALL =================
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    // ================= UPDATE FULL =================
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectCreateRequest request) {

        return ResponseEntity.ok(projectService.update(id, request));
    }

    // ================= UPDATE PARTIAL =================
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> patch(
            @PathVariable Long id,
            @RequestBody ProjectPatchRequest request) {

        return ResponseEntity.ok(projectService.patch(id, request));
    }

    // ================= UPDATE STATUS =================
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProjectStatusUpdateRequest request) {

        return ResponseEntity.ok(
                projectService.updateStatus(id, request.getStatus())
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

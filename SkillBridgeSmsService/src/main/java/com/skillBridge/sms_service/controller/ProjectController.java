package com.skillBridge.sms_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.ApplicationResponse;
import com.skillBridge.sms_service.dtos.ModifyProjectMembersRequest;
import com.skillBridge.sms_service.dtos.ProjectCreateRequest;
import com.skillBridge.sms_service.dtos.ProjectPatchRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.dtos.ProjectStatusUpdateRequest;
import com.skillBridge.sms_service.service.ApplicationService;
import com.skillBridge.sms_service.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;

    // ================= CREATE =================
    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                projectService.create(request, studentId)
        );
    }

    // ================= READ ONE (PUBLIC) =================
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.get(id));
    }

    // ================= READ ALL (PUBLIC) =================
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }
    


    // ================= UPDATE FULL =================
    @PutMapping("/update/{id}")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectCreateRequest request,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                projectService.update(id, request, studentId)
        );
    }

    // ================= UPDATE PARTIAL =================
    @PatchMapping("/patch/{id}")
    public ResponseEntity<ProjectResponse> patch(
            @PathVariable Long id,
            @RequestBody ProjectPatchRequest request,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                projectService.patch(id, request, studentId)
        );
    }

    // ================= UPDATE STATUS =================
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProjectStatusUpdateRequest request,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                projectService.updateStatus(id, request.getStatus(), studentId)
        );
    }
    
 // ================= MODIFY TEAM MEMBERS =================
    @PatchMapping("/{id}/members")
    public ResponseEntity<ProjectResponse> modifyProjectMembers(
            @PathVariable Long id,
            @RequestBody ModifyProjectMembersRequest request,
            @RequestHeader("X-USER-ID") Long requesterId
    ) {
        return ResponseEntity.ok(
                projectService.modifyProjectMembers(id, request, requesterId)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        projectService.delete(id, studentId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>> getProjectApplications(
            @PathVariable Long id, // projectId
            @RequestHeader("X-USER-ID") Long requesterId
    ) {
        return ResponseEntity.ok(
                applicationService.getApplicationsForProject(id, requesterId)
        );
    }
    @GetMapping("/{projectId}/members/{studentId}/exists")
    public ResponseEntity<Boolean> isMember(
            @PathVariable Long projectId,
            @PathVariable Long studentId
    ) {
        boolean isMember = projectService.isMember(projectId, studentId);
        return ResponseEntity.ok(isMember);
    }


}


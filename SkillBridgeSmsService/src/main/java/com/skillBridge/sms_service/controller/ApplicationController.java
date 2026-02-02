package com.skillBridge.sms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.ApplicationRequest;
import com.skillBridge.sms_service.dtos.ApplicationResponse;
import com.skillBridge.sms_service.dtos.ApplicationStatusUpdateRequest;
import com.skillBridge.sms_service.dtos.ApplicationUpdateRequest;
import com.skillBridge.sms_service.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // ================= CREATE (APPLY) =================
    @PostMapping("/create")
    public ResponseEntity<ApplicationResponse> create(
            @Valid @RequestBody ApplicationRequest request,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.create(request, studentId));
    }

    // ================= READ ONE =================
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> get(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                applicationService.get(id, studentId)
        );
    }
    
 // ================= VIEW APPLICATIONS FOR A PROJECT =================
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForProject(
            @PathVariable Long projectId,
            @RequestHeader("X-USER-ID") Long requesterId
    ) {
        return ResponseEntity.ok(
                applicationService.getApplicationsForProject(projectId, requesterId)
        );
    }


    // ================= READ MY APPLICATIONS =================
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll(
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                applicationService.getAll(studentId)
        );
    }

    // ================= UPDATE MESSAGE =================
    @PatchMapping("/update/{id}")
    public ResponseEntity<ApplicationResponse> updateMessage(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUpdateRequest request,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                applicationService.updateMessage(id, request, studentId)
        );
    }

    // ================= UPDATE STATUS =================
    @PatchMapping("/update/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return ResponseEntity.ok(
                applicationService.updateStatus(id, request, studentId)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        applicationService.delete(id, studentId);
        return ResponseEntity.noContent().build();
    }
}


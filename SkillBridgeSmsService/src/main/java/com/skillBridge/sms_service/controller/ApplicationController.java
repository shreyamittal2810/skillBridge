package com.skillBridge.sms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.ApplicationRequest;
import com.skillBridge.sms_service.dtos.ApplicationResponse;
import com.skillBridge.sms_service.dtos.ApplicationStatusUpdateRequest;
import com.skillBridge.sms_service.dtos.ApplicationUpdateRequest;
import com.skillBridge.sms_service.security.UserPrincipal;
import com.skillBridge.sms_service.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // ================= CREATE (APPLY) =================
    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @Valid @RequestBody ApplicationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.create(
                        request,
                        userPrincipal.getStudentId()
                ));
    }

    // ================= READ ONE =================
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> get(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(
                applicationService.get(id, userPrincipal.getStudentId())
        );
    }

    // ================= READ MY APPLICATIONS =================
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(
                applicationService.getAll(userPrincipal.getStudentId())
        );
    }

    // ================= UPDATE MESSAGE =================
    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationResponse> updateMessage(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(
                applicationService.updateMessage(
                        id,
                        request,
                        userPrincipal.getStudentId()
                )
        );
    }

    // ================= UPDATE STATUS (PROJECT OWNER) =================
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(
                applicationService.updateStatus(
                        id,
                        request,
                        userPrincipal.getStudentId()
                )
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        applicationService.delete(id, userPrincipal.getStudentId());
        return ResponseEntity.noContent().build();
    }
}

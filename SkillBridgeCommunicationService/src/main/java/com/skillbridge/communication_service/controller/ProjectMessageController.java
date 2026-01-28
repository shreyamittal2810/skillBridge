package com.skillbridge.communication_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillbridge.communication_service.dtos.ProjectMessageRequest;
import com.skillbridge.communication_service.dtos.ProjectMessageResponse;
import com.skillbridge.communication_service.dtos.ProjectMessageUpdateRequest;
import com.skillbridge.communication_service.entities.ProjectMessage;
import com.skillbridge.communication_service.service.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/project-messages")
@RequiredArgsConstructor
public class ProjectMessageController {

    private final MessageService messageService;

    // CREATE
    @PostMapping
    public ResponseEntity<ProjectMessageResponse> create(
            @RequestHeader("X-Student-Id") Long senderStudentId,
            @Valid @RequestBody ProjectMessageRequest request) {

        ProjectMessage msg = new ProjectMessage();
        msg.setProjectId(request.getProjectId());
        msg.setSenderStudentId(senderStudentId); // 🔐 from gateway
        msg.setMessage(request.getMessage());

        return new ResponseEntity<>(
                map(messageService.sendProjectMessage(msg)),
                HttpStatus.CREATED
        );
    }

    // READ (ALL for project)
    @GetMapping("/project/{projectId}")
    public List<ProjectMessageResponse> getByProject(@PathVariable Long projectId) {
        return messageService.getProjectMessages(projectId)
                .stream()
                .map(this::map)
                .toList();
    }

    // READ (ONE)
    @GetMapping("/{id}")
    public ProjectMessageResponse getById(@PathVariable Long id) {
        return map(messageService.getProjectMessageById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProjectMessageResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectMessageUpdateRequest request) {

        return map(
                messageService.updateProjectMessage(id, request.getMessage())
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.deleteProjectMessage(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ REQUIRED mapper method
    private ProjectMessageResponse map(ProjectMessage m) {
        ProjectMessageResponse r = new ProjectMessageResponse();
        r.setId(m.getId());
        r.setProjectId(m.getProjectId());
        r.setSenderStudentId(m.getSenderStudentId());
        r.setMessage(m.getMessage());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}

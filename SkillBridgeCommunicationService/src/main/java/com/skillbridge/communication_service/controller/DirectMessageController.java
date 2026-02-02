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

import com.skillbridge.communication_service.dtos.DirectMessageRequest;
import com.skillbridge.communication_service.dtos.DirectMessageResponse;
import com.skillbridge.communication_service.entities.DirectMessage;
import com.skillbridge.communication_service.service.MessageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/direct-messages")
public class DirectMessageController {

    private final MessageService messageService;

    public DirectMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<DirectMessageResponse> create(
            @RequestHeader("X-USER-ID") Long senderStudentId,
            @Valid @RequestBody DirectMessageRequest request
    ) {
        DirectMessage message = new DirectMessage();
        message.setSenderStudentId(senderStudentId);
        message.setReceiverStudentId(request.getReceiverStudentId());
        message.setMessage(request.getMessage());

        DirectMessage saved = messageService.sendDirectMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToResponse(saved));
    }

    // ---------------- READ (MY MESSAGES) ----------------
    @GetMapping("/me")
    public List<DirectMessageResponse> myMessages(
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        return messageService.getDirectMessagesForStudent(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public DirectMessageResponse update(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") Long studentId,
            @Valid @RequestBody DirectMessageRequest request
    ) {
        DirectMessage updated =
                messageService.updateDirectMessage(id, studentId, request.getMessage());

        return mapToResponse(updated);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") Long studentId
    ) {
        messageService.deleteDirectMessage(id, studentId);
        return ResponseEntity.noContent().build();
    }

    // ---------------- MAPPER ----------------
    private DirectMessageResponse mapToResponse(DirectMessage m) {
        DirectMessageResponse r = new DirectMessageResponse();
        r.setId(m.getId());
        r.setSenderStudentId(m.getSenderStudentId());
        r.setReceiverStudentId(m.getReceiverStudentId());
        r.setMessage(m.getMessage());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}

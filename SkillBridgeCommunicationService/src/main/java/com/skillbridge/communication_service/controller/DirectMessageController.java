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
import com.skillbridge.communication_service.dtos.DirectMessageUpdateRequest;
import com.skillbridge.communication_service.entities.DirectMessage;
import com.skillbridge.communication_service.service.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/direct-messages")
@RequiredArgsConstructor
public class DirectMessageController {

    private final MessageService messageService;

    // CREATE
    @PostMapping
    public ResponseEntity<DirectMessageResponse> create(
            @RequestHeader("X-Student-Id") Long senderStudentId,
            @Valid @RequestBody DirectMessageRequest request) {

        DirectMessage msg = new DirectMessage();
        msg.setSenderStudentId(senderStudentId); // 🔐 from gateway
        msg.setReceiverStudentId(request.getReceiverStudentId());
        msg.setMessage(request.getMessage());

        return new ResponseEntity<>(
                map(messageService.sendDirectMessage(msg)),
                HttpStatus.CREATED
        );
    }

    // READ (ALL for logged-in student)
    @GetMapping("/me")
    public List<DirectMessageResponse> getForMe(
            @RequestHeader("X-Student-Id") Long studentId) {

        return messageService.getDirectMessagesForStudent(studentId)
                .stream()
                .map(this::map)
                .toList();
    }

    // READ (ONE)
    @GetMapping("/{id}")
    public DirectMessageResponse getById(@PathVariable Long id) {
        return map(messageService.getDirectMessageById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public DirectMessageResponse update(
            @PathVariable Long id,
            @Valid @RequestBody DirectMessageUpdateRequest request) {

        return map(
                messageService.updateDirectMessage(id, request.getMessage())
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.deleteDirectMessage(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ REQUIRED mapper method (THIS FIXES THE ERROR)
    private DirectMessageResponse map(DirectMessage m) {
        DirectMessageResponse r = new DirectMessageResponse();
        r.setId(m.getId());
        r.setSenderStudentId(m.getSenderStudentId());
        r.setReceiverStudentId(m.getReceiverStudentId());
        r.setMessage(m.getMessage());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}

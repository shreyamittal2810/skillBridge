package com.skillbridge.communication_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillbridge.communication_service.client.ProjectClient;
import com.skillbridge.communication_service.client.StudentClient;
import com.skillbridge.communication_service.entities.DirectMessage;
import com.skillbridge.communication_service.exception.ResourceNotFoundException;
import com.skillbridge.communication_service.repository.DirectMessageRepository;
import com.skillbridge.communication_service.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  
    private final DirectMessageRepository directMessageRepository;
    private final StudentClient studentClient;
    private final ProjectClient projectClient;


    // ================= PROJECT CHAT =================

  

    // ================= DIRECT CHAT =================

    @Override
    public DirectMessage sendDirectMessage(DirectMessage message) {
        validateDirectMessage(message);
        return directMessageRepository.save(message);
    }

    @Override
    public List<DirectMessage> getDirectMessagesForStudent(Long studentId) {
        return directMessageRepository
                .findBySenderStudentIdOrReceiverStudentIdOrderByCreatedAtAsc(
                        studentId, studentId
                );
    }

    @Override
    public DirectMessage getDirectMessageById(Long id) {
        return directMessageRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Direct message not found with id: " + id
                )
            );
    }



    @Override
    public DirectMessage updateDirectMessage(Long id, Long studentId, String newMessage) {
        DirectMessage message = getDirectMessageById(id);

        verifySender(message.getSenderStudentId(), studentId);
        validateMessageContent(newMessage);

        message.setMessage(newMessage);
        return directMessageRepository.save(message);
    }

    @Override
    public void deleteDirectMessage(Long id, Long studentId) {
        DirectMessage message = getDirectMessageById(id);
        verifySender(message.getSenderStudentId(), studentId);
        directMessageRepository.delete(message);
    }

    // ================= VALIDATION HELPERS =================

    private void verifySender(Long actualSenderId, Long requesterId) {
        if (!actualSenderId.equals(requesterId)) {
            throw new RuntimeException("Unauthorized operation");
        }
    }

    private void validateMessageContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
    }

   
    private void validateDirectMessage(DirectMessage message) {

        if (message.getSenderStudentId() == null) {
            throw new IllegalArgumentException("Sender ID is required");
        }

        if (message.getReceiverStudentId() == null) {
            throw new IllegalArgumentException("Receiver ID is required");
        }
        if (message.getSenderStudentId().equals(message.getReceiverStudentId())) {
            throw new IllegalArgumentException(
                "Sender and receiver cannot be the same"
            );
        }
        if (!studentClient.studentExists(message.getReceiverStudentId())) {
            throw new ResourceNotFoundException(
                "Receiver student does not exist: " + message.getReceiverStudentId()
            );
        }

        validateMessageContent(message.getMessage());
    }



}

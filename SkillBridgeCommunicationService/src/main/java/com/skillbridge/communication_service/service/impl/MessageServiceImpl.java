package com.skillbridge.communication_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillbridge.communication_service.entities.DirectMessage;
import com.skillbridge.communication_service.entities.ProjectMessage;
import com.skillbridge.communication_service.exception.ResourceNotFoundException;
import com.skillbridge.communication_service.repository.DirectMessageRepository;
import com.skillbridge.communication_service.repository.ProjectMessageRepository;
import com.skillbridge.communication_service.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final ProjectMessageRepository projectMessageRepository;
    private final DirectMessageRepository directMessageRepository;

    // ---------------- Project Chat ----------------

    @Override
    public ProjectMessage sendProjectMessage(ProjectMessage message) {
        return projectMessageRepository.save(message);
    }

    @Override
    public List<ProjectMessage> getProjectMessages(Long projectId) {
        return projectMessageRepository.findByProjectIdOrderByCreatedAtAsc(projectId);
    }
    
    @Override
    public ProjectMessage getProjectMessageById(Long id) {
        return projectMessageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project message not found with id: " + id));
    }


    @Override
    public ProjectMessage updateProjectMessage(Long id, String newMessage) {
        ProjectMessage message = getProjectMessageById(id);
        message.setMessage(newMessage);
        return projectMessageRepository.save(message);
    }

    @Override
    public void deleteProjectMessage(Long id) {
        projectMessageRepository.deleteById(id);
    }


    // ---------------- Direct Chat ----------------

    @Override
    public DirectMessage sendDirectMessage(DirectMessage message) {
        return directMessageRepository.save(message);
    }

    @Override
    public List<DirectMessage> getDirectMessagesForStudent(Long studentId) {
        return directMessageRepository
                .findBySenderStudentIdOrReceiverStudentIdOrderByCreatedAtAsc(
                        studentId,
                        studentId
                );
    }
    
    @Override
    public DirectMessage getDirectMessageById(Long id) {
        return directMessageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Direct message not found with id: " + id));
    }

    @Override
    public DirectMessage updateDirectMessage(Long id, String newMessage) {
        DirectMessage message = getDirectMessageById(id);
        message.setMessage(newMessage);
        return directMessageRepository.save(message);
    }

    @Override
    public void deleteDirectMessage(Long id) {
        directMessageRepository.deleteById(id);
    }
    
    
}

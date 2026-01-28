package com.skillbridge.communication_service.service;

import java.util.List;

import com.skillbridge.communication_service.entities.DirectMessage;
import com.skillbridge.communication_service.entities.ProjectMessage;

public interface MessageService {

    // PROJECT MESSAGE
    ProjectMessage sendProjectMessage(ProjectMessage message);
    List<ProjectMessage> getProjectMessages(Long projectId);
    ProjectMessage getProjectMessageById(Long id);
    ProjectMessage updateProjectMessage(Long id, String message);
    void deleteProjectMessage(Long id);

    // DIRECT MESSAGE
    DirectMessage sendDirectMessage(DirectMessage message);
    List<DirectMessage> getDirectMessagesForStudent(Long studentId);
    DirectMessage getDirectMessageById(Long id);
    DirectMessage updateDirectMessage(Long id, String message);
    void deleteDirectMessage(Long id);
}

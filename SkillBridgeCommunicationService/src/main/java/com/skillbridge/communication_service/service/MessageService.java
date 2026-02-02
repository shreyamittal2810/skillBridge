package com.skillbridge.communication_service.service;

import java.util.List;

import com.skillbridge.communication_service.entities.DirectMessage;

public interface MessageService {

   
   

    // DIRECT CHAT
    DirectMessage sendDirectMessage(DirectMessage message);
    List<DirectMessage> getDirectMessagesForStudent(Long studentId);
    DirectMessage updateDirectMessage(Long id, Long studentId, String newMessage);
    void deleteDirectMessage(Long id, Long studentId);

	DirectMessage getDirectMessageById(Long id);
}


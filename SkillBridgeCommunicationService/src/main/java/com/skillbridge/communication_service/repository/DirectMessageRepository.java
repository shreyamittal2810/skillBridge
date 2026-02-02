package com.skillbridge.communication_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillbridge.communication_service.entities.DirectMessage;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {

    // Fetch all messages where student is sender OR receiver
    List<DirectMessage> findBySenderStudentIdOrReceiverStudentIdOrderByCreatedAtAsc(
            Long senderStudentId,
            Long receiverStudentId
    );
    
    List<DirectMessage> findByReceiverStudentId(Long receiverStudentId);
}

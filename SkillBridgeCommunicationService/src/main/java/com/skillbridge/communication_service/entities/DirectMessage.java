package com.skillbridge.communication_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "direct_messages")
@Getter
@Setter
public class DirectMessage extends BaseMessageEntity {

    // Receiver student ID (who receives the message)
    private Long receiverStudentId;

    // Actual chat content
    private String message;
}

package com.skillbridge.communication_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "project_messages")
@Getter
@Setter
public class ProjectMessage extends BaseMessageEntity {

    // Project ID from Student Service
    private Long projectId;

    // Actual chat content
    private String message;
}

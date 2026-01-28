package com.skillbridge.communication_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillbridge.communication_service.entities.ProjectMessage;

@Repository
public interface ProjectMessageRepository extends JpaRepository<ProjectMessage, Long> {

    // Fetch all messages of a specific project
    List<ProjectMessage> findByProjectIdOrderByCreatedAtAsc(Long projectId);
}

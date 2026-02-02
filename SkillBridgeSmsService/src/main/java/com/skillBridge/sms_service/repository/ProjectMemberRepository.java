package com.skillBridge.sms_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillBridge.sms_service.entities.ProjectMember;

public interface ProjectMemberRepository
        extends JpaRepository<ProjectMember, Long> {

    boolean existsByProject_ProjectIdAndStudentId(
            Long projectId,
            Long studentId
    );
}

package com.skillBridge.sms_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillBridge.sms_service.entities.Application;


public interface ApplicationRepository extends JpaRepository<Application,Long> {

	boolean existsByProjectIdAndStudentId(Long projectId, Long senderId);

	List<Application> findByStudentId(Long studentId);
	
	List<Application> findByProjectId(Long projectId);

}

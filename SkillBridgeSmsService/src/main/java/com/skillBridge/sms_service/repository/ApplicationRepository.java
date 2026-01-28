package com.skillBridge.sms_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillBridge.sms_service.entities.Application;


public interface ApplicationRepository extends JpaRepository<Application,Long> {

	boolean existsByProjectIdAndSenderId(Long projectId, Long senderId);

	Optional<Application> findBySenderId(Long studentId);



}

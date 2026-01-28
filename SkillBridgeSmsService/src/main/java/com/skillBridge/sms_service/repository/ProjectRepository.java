package com.skillBridge.sms_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillBridge.sms_service.entities.Project;


public interface ProjectRepository extends JpaRepository<Project,Long> {



}

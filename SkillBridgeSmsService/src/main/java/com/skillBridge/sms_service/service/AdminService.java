package com.skillBridge.sms_service.service;

import java.util.List;

import com.skillBridge.sms_service.dtos.AdminRegisterRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.dtos.StudentResponse;
import com.skillBridge.sms_service.entities.ProjectStatus;
import com.skillBridge.sms_service.entities.Role;

public interface AdminService {

    StudentResponse create(AdminRegisterRequest request);

    List<StudentResponse> getAll();

    StudentResponse getById(Long id);

    void updateRole(Long id, Role role);

    void delete(Long id);
    
    //=========PROJECT ADMIN ===============
    List<ProjectResponse> getAllProjects();

    ProjectResponse updateProjectStatus(Long projectId, ProjectStatus status);

    void deleteProject(Long projectId);

	
}

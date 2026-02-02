package com.skillBridge.sms_service.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.AdminRegisterRequest;
import com.skillBridge.sms_service.dtos.ProjectResponse;
import com.skillBridge.sms_service.dtos.StudentResponse;
import com.skillBridge.sms_service.entities.ProjectStatus;
import com.skillBridge.sms_service.entities.Role;
import com.skillBridge.sms_service.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ✅ 1. Create a new admin
    @PostMapping("/register")
    public ResponseEntity<StudentResponse> createAdmin(
            @Valid @RequestBody AdminRegisterRequest request
    ) {
        return new ResponseEntity<>(
                adminService.create(request),
                HttpStatus.CREATED
        );
    }

    // ✅ 2. Get all students
    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAll());
    }

    // ✅ 3. Get student by ID
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(adminService.getById(id));
    }

    // ✅ 4. Change role (STUDENT ↔ ADMIN)
    @PatchMapping("/students/{id}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable Long id,
            @RequestParam Role role
    ) {
        adminService.updateRole(id, role);
        return ResponseEntity.noContent().build();
    }

    // ✅ 5. Delete a student
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id
    ) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
 // 6️⃣ Get all projects
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(adminService.getAllProjects());
    }

    // 7️⃣ Update project status (OPEN / CLOSED / CANCELLED) 
    @PatchMapping("/projects/{id}/status")
    public ResponseEntity<ProjectResponse> updateProjectStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status
    ) {
        return ResponseEntity.ok(
                adminService.updateProjectStatus(id, status)
        );
    }

    // 8️⃣ Delete any project
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id
    ) {
        adminService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    
    
}

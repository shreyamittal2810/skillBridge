package com.skillBridge.sms_service.service;

import java.util.List;

import com.skillBridge.sms_service.dtos.StudentRegisterRequest;
import com.skillBridge.sms_service.dtos.StudentResponse;
import com.skillBridge.sms_service.dtos.StudentUpdateRequest;
import com.skillBridge.sms_service.entities.Student;

public interface StudentService {

    // CREATE
    StudentResponse create(StudentRegisterRequest request);

    // READ
    StudentResponse get(Long id);
    List<StudentResponse> getAll();

    // UPDATE
    StudentResponse update(Long id, StudentRegisterRequest request);
    StudentResponse patch(Long id, StudentUpdateRequest request);

    // DELETE
    void delete(Long id);

    // 🔐 AUTH (ADD THIS)
    Student authenticate(String email, String password);

	void assertStudentExists(Long id);
}

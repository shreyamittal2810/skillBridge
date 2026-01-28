package com.skillBridge.sms_service.service;

import java.util.List;

import com.skillBridge.sms_service.dtos.AdminRegisterRequest;
import com.skillBridge.sms_service.dtos.AdminResponse;
import com.skillBridge.sms_service.dtos.StudentResponse;

import jakarta.validation.Valid;

public interface AdminService {

	StudentResponse create(@Valid AdminRegisterRequest request);
	List<StudentResponse> getAll();

}

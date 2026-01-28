package com.skillBridge.sms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.AdminRegisterRequest;
import com.skillBridge.sms_service.dtos.AdminResponse;
import com.skillBridge.sms_service.dtos.StudentResponse;
import com.skillBridge.sms_service.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	
	//Create an account for a new admin
	@PostMapping("/register")
	public ResponseEntity<StudentResponse> create(
			@Valid @RequestBody AdminRegisterRequest request){
		return new ResponseEntity<>(
				adminService.create(request),
				HttpStatus.CREATED
				);
	}
	//Read all the students
    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAll() {
        return ResponseEntity.ok(adminService.getAll());
    }


}

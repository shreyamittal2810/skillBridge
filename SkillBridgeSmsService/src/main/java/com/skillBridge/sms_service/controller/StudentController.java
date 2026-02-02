package com.skillBridge.sms_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.StudentRegisterRequest;
import com.skillBridge.sms_service.dtos.StudentResponse;
import com.skillBridge.sms_service.dtos.StudentUpdateRequest;
import com.skillBridge.sms_service.security.UserPrincipal;
import com.skillBridge.sms_service.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // CREATE
    @PostMapping("/register")
    public ResponseEntity<StudentResponse> create(
            @Valid @RequestBody StudentRegisterRequest request
    ) {
        return new ResponseEntity<>(
                studentService.create(request),
                HttpStatus.CREATED
        );
    }

    // READ ONE
    @GetMapping("/info")
    public ResponseEntity<StudentResponse> get(@RequestHeader("X-USER-ID") Long studentId) {
        
    	
    	return ResponseEntity.ok(studentService.get(studentId));
    }
    
    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> studentExists(@PathVariable Long id) {
        studentService.assertStudentExists(id);
        return ResponseEntity.ok().build();
    }


    // READ ALL

    // UPDATE FULL
    @PutMapping("/update")
    public ResponseEntity<StudentResponse> update(
            @RequestHeader("X-USER-ID") Long id,
            @Valid @RequestBody StudentRegisterRequest request
    ) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    // UPDATE PARTIAL
    @PatchMapping("/patch")
    public ResponseEntity<StudentResponse> patch(
            @RequestHeader("X-USER-ID") Long id,
            @RequestBody StudentUpdateRequest request
    ) {
        return ResponseEntity.ok(studentService.patch(id, request));
    }

    // DELETE
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestHeader("X-USER-IDO") Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

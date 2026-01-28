package com.skillBridge.sms_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillBridge.sms_service.dtos.AuthRequest;
import com.skillBridge.sms_service.dtos.StudentAuthResponse;
import com.skillBridge.sms_service.entities.Student;
import com.skillBridge.sms_service.security.JwtUtils;
import com.skillBridge.sms_service.service.StudentService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthInternalController {

    private final StudentService studentService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<StudentAuthResponse> login(
            @RequestBody AuthRequest request
    ) {
        Student student = studentService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtils.generateToken(
                student.getStudentId(),
                student.getEmail(),
                student.getRole().name()
        );

        return ResponseEntity.ok(
                new StudentAuthResponse(
                        student.getStudentId(),
                        student.getEmail(),
                        token
                )
        );
    }
}

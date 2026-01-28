package com.skillBridge.sms_service.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skillBridge.sms_service.dtos.AdminRegisterRequest;
import com.skillBridge.sms_service.dtos.StudentResponse;
import com.skillBridge.sms_service.entities.Role;
import com.skillBridge.sms_service.entities.Skill;
import com.skillBridge.sms_service.entities.Student;
import com.skillBridge.sms_service.entities.StudentSkill;
import com.skillBridge.sms_service.repository.StudentRepository;
import com.skillBridge.sms_service.service.AdminService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
	
	private final StudentRepository studentRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public StudentResponse create(@Valid AdminRegisterRequest request) {

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setCourse(request.getCourse());
        student.setRole(Role.ADMIN);

        // ✅ map skills properly
        request.getSkills().forEach(skillStr -> {
            StudentSkill ss = new StudentSkill();
            ss.setSkill(Skill.valueOf(skillStr.toUpperCase()));
            ss.setStudent(student);
            student.getSkills().add(ss);
        });

        return mapToResponse(studentRepository.save(student));
	}
	
    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
            student.getStudentId(),
            student.getName(),
            student.getEmail(),
            student.getCourse(),
            student.getSkills().stream()
                .map(ss -> ss.getSkill().name())
                .collect(Collectors.toSet())
        );
    }

	@Override
	@Transactional
	public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
	}

}

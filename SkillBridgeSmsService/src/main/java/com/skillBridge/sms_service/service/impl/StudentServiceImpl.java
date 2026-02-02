package com.skillBridge.sms_service.service.impl;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.skillBridge.sms_service.dtos.StudentRegisterRequest;
import com.skillBridge.sms_service.dtos.StudentResponse;
import com.skillBridge.sms_service.dtos.StudentUpdateRequest;
import com.skillBridge.sms_service.entities.Skill;
import com.skillBridge.sms_service.entities.Student;
import com.skillBridge.sms_service.entities.StudentSkill;
import com.skillBridge.sms_service.exception.ResourceNotFoundException;
import com.skillBridge.sms_service.repository.StudentRepository;
import com.skillBridge.sms_service.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @Override
    public StudentResponse create(StudentRegisterRequest request) {

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setCourse(request.getCourse());

        // ✅ map skills properly
        request.getSkills().forEach(skillStr -> {
            StudentSkill ss = new StudentSkill();
            ss.setSkill(Skill.valueOf(skillStr.toUpperCase()));
            ss.setStudent(student);
            student.getSkills().add(ss);
        });

        return mapToResponse(studentRepository.save(student));
    }

    // READ ONE
    @Override
    @Transactional(readOnly = true)
    public StudentResponse get(Long id) {
        return mapToResponse(
            studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"))
        );
    }



    // UPDATE FULL
    @Override
    public StudentResponse update(Long id, StudentRegisterRequest request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setCourse(request.getCourse());

        // 🔁 replace skills
        student.getSkills().clear();
        request.getSkills().forEach(skillStr -> {
            StudentSkill ss = new StudentSkill();
            ss.setSkill(Skill.valueOf(skillStr));
            ss.setStudent(student);
            student.getSkills().add(ss);
        });

        return mapToResponse(studentRepository.save(student));
    }

    // UPDATE PARTIAL
    @Override
    public StudentResponse patch(Long id, StudentUpdateRequest request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        if (request.getName() != null)
            student.setName(request.getName());

        if (request.getCourse() != null)
            student.setCourse(request.getCourse());

        if (request.getSkills() != null) {
            student.getSkills().clear();
            request.getSkills().forEach(skillStr -> {
                StudentSkill ss = new StudentSkill();
                ss.setSkill(Skill.valueOf(skillStr));
                ss.setStudent(student);
                student.getSkills().add(ss);
            });
        }

        return mapToResponse(studentRepository.save(student));
    }

    // DELETE
    @Override
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalStateException("Student not found");
        }
        studentRepository.deleteById(id);
    }

    // 🔁 
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
    public Student authenticate(String email, String password) {

        Student student = studentRepository.findByEmail(email)
                .orElse(null);

        if (student == null) {
            return null;
        }

        if (!passwordEncoder.matches(password, student.getPassword())) {
            return null;
        }

        return student;
    }

	@Override
	public void assertStudentExists(Long id) {
		if (!studentRepository.existsById(id)) {
	        throw new ResponseStatusException(
	            HttpStatus.NOT_FOUND,
	            "Student not found"
	        );
	    }
	}

}

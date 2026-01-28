package com.skillBridge.sms_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillBridge.sms_service.entities.Student;


public interface StudentRepository extends JpaRepository<Student,Long> {

	boolean existsByEmail(String email);

	Optional<Student> findByEmail(String email);



}

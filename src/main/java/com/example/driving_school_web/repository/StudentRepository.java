package com.example.driving_school_web.repository;

import com.example.driving_school_web.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {}
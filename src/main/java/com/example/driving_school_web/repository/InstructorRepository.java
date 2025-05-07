package com.example.driving_school_web.repository;

import com.example.driving_school_web.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {}
package com.example.driving_school_web.controller;

import com.example.driving_school_web.model.Instructor;
import com.example.driving_school_web.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@CrossOrigin(origins = "http://localhost:5173")
public class InstructorController {
    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
        return instructorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Instructor createInstructor(@RequestBody Instructor instructor) {
        System.out.println(instructor);
        return instructorRepository.save(instructor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instructor> updateInstructor(@PathVariable Long id, @RequestBody Instructor instructor) {
        return instructorRepository.findById(id)
                .map(existingInstructor -> {
                    existingInstructor.setInstructorName(instructor.getInstructorName());
                    existingInstructor.setPhone(instructor.getPhone());
                    existingInstructor.setHireDate(instructor.getHireDate());
                    return ResponseEntity.ok(instructorRepository.save(existingInstructor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        return instructorRepository.findById(id)
                .map(instructor -> {
                    instructorRepository.delete(instructor);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

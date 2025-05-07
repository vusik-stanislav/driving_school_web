package com.example.driving_school_web.controller;

import com.example.driving_school_web.model.Group;
import com.example.driving_school_web.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "http://localhost:5173")
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group group) {
        return groupRepository.findById(id)
                .map(existingGroup -> {
                    existingGroup.setGroupName(group.getGroupName());
                    existingGroup.setStartDate(group.getStartDate());
                    existingGroup.setEndDate(group.getEndDate());
                    existingGroup.setInstructor(group.getInstructor());
                    return ResponseEntity.ok(groupRepository.save(existingGroup));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(group -> {
                    groupRepository.delete(group);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
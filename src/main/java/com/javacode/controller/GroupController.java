package com.javacode.controller;

import com.javacode.dto.GroupDTO;
import com.javacode.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDTO> create(@RequestBody GroupDTO dto) {
        GroupDTO response = groupService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(groupService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,@RequestBody GroupDTO dto) {
        groupService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        groupService.delete(id);
        return ResponseEntity.ok().build();
    }
}

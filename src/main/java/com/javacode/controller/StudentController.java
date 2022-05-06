package com.javacode.controller;

import com.javacode.dto.RoleDTO;
import com.javacode.dto.RoleToUserFromDTO;
import com.javacode.dto.StudentDTO;
import com.javacode.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/public")
    public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO dto) {
        StudentDTO response = studentService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/role/save")
    public ResponseEntity<?> saveRole(@RequestBody RoleDTO role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(studentService.createRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserFromDTO from) {
        studentService.addRoleToUser(from.getUsername(), from.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<StudentDTO> dtoList = studentService.getAll();
        return dtoList != null ? ResponseEntity.ok(dtoList) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @GetMapping("/groupId/{groupId}")
    public ResponseEntity<?> getByGroupId(@PathVariable("groupId") Integer id) {
        return ResponseEntity.ok(studentService.getByGroupId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody StudentDTO dto) {
        studentService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }
}

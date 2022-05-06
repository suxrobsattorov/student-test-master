package com.javacode.service;

import com.javacode.dto.RoleDTO;
import com.javacode.dto.StudentDTO;
import com.javacode.entity.GroupEntity;
import com.javacode.entity.RoleEntity;
import com.javacode.entity.StudentEntity;
import com.javacode.repository.RoleRepository;
import com.javacode.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentService implements UserDetailsService {

    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentEntity user = studentRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public StudentDTO create(StudentDTO dto) {

        GroupEntity groupEntity = groupService.get(dto.getGroupId());
        StudentEntity entity = new StudentEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setLevel(dto.getLevel());
        entity.setUsername(dto.getUsername());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setGroup(groupEntity);
        studentRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public RoleDTO createRole(RoleDTO dto) {
        RoleEntity entity = new RoleEntity();
        entity.setName(dto.getName());
        roleRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<StudentDTO> getAll() {
        List<StudentEntity> entityList = studentRepository.findAll();
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public StudentDTO getById(Integer id) {
        StudentEntity entity = get(id);
        return toDTO(entity);
    }

    public List<StudentDTO> getByGroupId(Integer groupId) {
        return getAll().stream().filter(dto -> dto.getGroupId().equals(groupId)).collect(Collectors.toList());
    }

    public void update(Integer id, StudentDTO dto) {
        StudentEntity entity = get(id);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        studentRepository.save(entity);
    }

    public void delete(Integer id) {
        StudentEntity entity = get(id);
        studentRepository.delete(entity);
    }

    public StudentDTO toDTO(StudentEntity entity) {
        Collection<RoleDTO> entities = new ArrayList<>();
        StudentDTO dto = new StudentDTO();
        for (RoleEntity role : entity.getRoles()) {
            entities.add(toDTORole(role));
        }
        dto.setRoleDTOS(entities);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setLevel(entity.getLevel());
        dto.setUsername(entity.getUsername());
        dto.setGroupId(entity.getGroup().getId());
        return dto;
    }

    public RoleDTO toDTORole(RoleEntity entity) {
        RoleDTO dto = new RoleDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public void addRoleToUser(String username, String roleName) {
        StudentEntity student = studentRepository.findByUsername(username);
        RoleEntity role = roleRepository.findByName(roleName);
        student.getRoles().add(role);
    }

    public StudentEntity get(Integer id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }
}

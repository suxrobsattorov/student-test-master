package com.javacode.service;

import com.javacode.dto.GroupDTO;
import com.javacode.entity.GroupEntity;
import com.javacode.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public GroupDTO create(GroupDTO dto) {
        GroupEntity entity = new GroupEntity();
        entity.setGroupName(dto.getGroupName());
        groupRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<GroupDTO> getAll() {
        List<GroupEntity> entityList = groupRepository.findAll();
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public GroupDTO getById(Integer id) {
        GroupEntity entity = get(id);
        return toDTO(entity);
    }

    public void update(Integer id, GroupDTO dto) {
        GroupEntity entity = get(id);
        entity.setGroupName(dto.getGroupName());
        groupRepository.save(entity);
    }

    public void delete(Integer id) {
        GroupEntity entity = get(id);
        groupRepository.delete(entity);
    }

    public GroupDTO toDTO(GroupEntity entity) {
        GroupDTO dto = new GroupDTO();
        dto.setId(entity.getId());
        dto.setGroupName(entity.getGroupName());
        return dto;
    }

    public GroupEntity get(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
    }
}

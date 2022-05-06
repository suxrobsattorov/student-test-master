package com.javacode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
    private Integer id;
    private String name;
    private String surname;
    private String level;
    private String username;
    private String password;
    private Integer groupId;
    private Collection<RoleDTO> roleDTOS = new ArrayList<>();
    private String jwt;
}

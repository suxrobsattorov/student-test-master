package com.javacode.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String groupName;
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<StudentEntity> studentEntities;
}

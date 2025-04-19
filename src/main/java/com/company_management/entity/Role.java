package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

//    @Schema(description = "Mã role")
    @Column(name = "CODE")
    private String code;

//    @Schema(description = "Tên role")
    @Column(name = "NAME")
    private String name;

//    @Schema(description = "Trạng thái hoạt động")
//    @Convert(converter = BooleanNumberConverter.class)
    @Column(name = "ACTIVE")
    private Boolean active;// Trạng thái kích hoạt: true = active, false = inactive

    @Column(name = "IS_MASTER")
    private Boolean isMaster;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Permission> permission;

    @ManyToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinTable(name = "menu_role_mapping", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<Menu> menus;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "employee_role_mapping", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employees;
}

package com.company_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DEPARTMENT") // Bảng phòng ban
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DEPARTMENT_CODE")
    private String departmentCode; // ma phong ban

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName; // ten phong ban

    @Column(name = "STATUS")
    private Integer status ;
}

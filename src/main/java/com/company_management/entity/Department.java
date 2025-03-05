package com.company_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "DEPARTMENT_CODE")
    private String departmentCode; // ma phong ban

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName; // ten phong ban

}

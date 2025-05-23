package com.company_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ALLOWANCE") //Lương phụ cấp
public class Allowance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ALLOWANCE_CODE")
    private String allowanceCode;

    @Column(name = "ALLOWANCE_NAME")
    private String allowanceName;

    @Column(name = "ALLOWANCE_BASE")
    private Double allowanceBase;

    @Column(name = "ALLOWANCE_DESCRIPTION")
    private String allowanceDescription;

    @Column(name = "ATTACH_FILE")
    private String attachFile;

    @ManyToMany(mappedBy = "allowances", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Employee> employees;

    @Column(name = "STATUS")
    private Integer status ;
}

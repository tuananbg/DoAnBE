package com.company_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WAGE") //Lương phụ cấp
public class Wage extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "id")
    private Employee employee;

    @Column(name = "WAGE_NAME")
    private String wageName;

    @Column(name = "WAGE_BASE")
    private Double wageBase; //luong phu cap co ban( số tien)

    @Column(name = "WAGE_DESCRIPTION")
    private String wageDescription;

    @Column(name = "EMP_SIGN")
    private String empSign;

    @Column(name = "LICENSE_DATE")
    private Date licenseDate;

    @Column(name = "ATTACH_FILE")
    private String attachFile;  //file thong tin phu cap

}

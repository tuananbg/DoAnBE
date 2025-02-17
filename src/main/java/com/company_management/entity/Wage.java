package com.company_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "USER_DETAIL_ID")
    private Long userDetailId;

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

package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "EMPLOYEE_INFO") //Chi tiết CBNV
public class EmployeeInfo extends BaseEntity {

    @Column(name = "DATE_OF_BIRTH")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date dateOfBirth;

    //giới tính
    @Column(name = "GENDER")
    private int gender;

    //Nơi sinh
    @Column(name= "PLACE_OF_BIRTH")
    private String placeOfBirth;

    //Mã số thuế
    @Column(name="TAX_CODE")
    private String taxCode;

    //Số CMND/CCCD
    @Column(name = "INSURANCE_NUMBER")
    private String insuranceNumber;

    //Số tài khoản
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name="EMAIL")
    private String email;

    // địa chỉ thường trú
    @Column(name = "PERMANENT_ADDRESS")
    private String permanentAddress;

    //Số bảo hiểm
    @Column(name = "IDENTITY_NUMBER")
    private String identityNumber;

    //Điện thoại
    @Column(name = "MOBILE")
    private String mobile;

    @OneToOne(mappedBy = "employeeInfo")
    private Employee employee;
}

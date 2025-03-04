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
@Table(name = "EMPLOYEE_INFO")
public class EmployeeInfo extends BaseEntity {

    @Column(name = "DATE_OF_BIRTH")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date dateOfBirth;

    @Column(name = "GENDER")
    private int gender;

    @Column(name= "PLACE_OF_BIRTH")
    private String placeOfBirth;

    @Column(name="TAX_CODE")
    private String taxCode;

    @Column(name = "INSURANCE_NUMBER")
    private String insuranceNumber;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "PERMANENT_ADDRESS")
    private String permanentAddress;

    @Column(name ="CURRENT_ADDRESS")
    private String currentAddress;

    @Column(name = "IDENTITY_NUMBER")
    private String identityNumber;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "NATION")
    private String nation;
}

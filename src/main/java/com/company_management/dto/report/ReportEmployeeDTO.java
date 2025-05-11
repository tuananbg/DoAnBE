package com.company_management.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportEmployeeDTO {
    private String employeeCode;
    private String employeeName;
    private String genderName;
    private Date dateOfBirth;
    private String yearOld;
    private String email;
    private String placeOfBirth;
    private String departmentCode;
    private String departmentName;
    private String positionCode;
    private String positionName;
    private String taxCode;
    private String insuranceNumber;
    private String accountNumber;
    private String permanentAddress;
    private String currentAddress;
    private String identityNumber;
    private String mobile;
    private String nation;
    private String statusName;
}

package com.company_management.dto.response.pa;

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
    private Date birthday;
    private String email;
    private String address;
    private String departmentName;
    private String positionName;
}

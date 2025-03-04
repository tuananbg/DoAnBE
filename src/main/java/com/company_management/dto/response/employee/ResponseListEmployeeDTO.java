package com.company_management.dto.response.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseListEmployeeDTO {
    private Long id;

    private String employeeCode;

    private String employeeName;

    private Integer gender;

    private Date birthday;

    private String phone;

    private String address;

    private Integer isActive;

    private String departmentName;

    private String positionName;

}

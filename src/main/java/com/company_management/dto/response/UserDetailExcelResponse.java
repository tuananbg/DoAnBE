package com.company_management.dto.response;

import com.company_management.dto.ContractDTO;
import com.company_management.dto.SocialInsuranceDTO;
import com.company_management.dto.WageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailExcelResponse {
    private int index;
    private Long id;
    private String employeeCode;
    private String employeeName;
    private Integer gender;
    private String genderName;
    private String birthday;
    private String avatar;
    private String email;
    private String address;
    private Integer isActive;
    private Long departmentId;
    private String departmentName;
}

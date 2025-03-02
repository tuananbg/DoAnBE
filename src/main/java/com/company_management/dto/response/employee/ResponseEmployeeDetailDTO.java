package com.company_management.dto.response.employee;

import com.company_management.dto.ContractDTO;
import com.company_management.dto.SocialInsuranceDTO;
import com.company_management.dto.WageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEmployeeDetailDTO {

    private Long id;

    private String employeeCode;

    private String employeeName;

    private Integer gender;

    private Date birthday;

    private String avatar;

    private String phone;

    private String email;

    private String address;

    private Integer isActive;

    private String departmentName;

    private String positionName;

    private List<ContractDTO> contracts;

    private List<SocialInsuranceDTO> socialInsurances;

    private List<WageDTO> wages;
}

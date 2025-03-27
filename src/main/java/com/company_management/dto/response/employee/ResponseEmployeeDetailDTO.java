package com.company_management.dto.response.employee;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEmployeeDetailDTO {

    private Long id;

    private String code;

    private String fullName;

    private String avatar;

    private String departmentName;

    private String positionName;

    private String seatCode;

    private ResponseEmployeeInfoDTO employeeInfo;

    private List<ResponseEmployeeDetailContractsDTO> contracts;

    private List<ResponseEmployeeDetailSocialDTO> socialInsurances;

    private List<ResponseEmployeeDetailWageDTO> wages;

    private List<ResponseEmployeeDetailQualificationDTO> qualifications;
}

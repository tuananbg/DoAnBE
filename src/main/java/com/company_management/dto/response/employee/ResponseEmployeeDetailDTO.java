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

    //Mã CBNV
    private String employeeCode;

    //Họ tên
    private String fullName;

    //Ảnh đại diện
    private String avatar;

    //Phòng ban
    private String departmentName;

    //Chức vụ
    private String positionName;

    private ResponseEmployeeInfoDTO employeeInfo;
}

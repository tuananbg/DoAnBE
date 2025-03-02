package com.company_management.dto.request.employee;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmployeeDetailDTO {

    @NotBlank(message = "Mã nhân viên không được để trống!")
    private String employeeCode;

    @NotBlank(message = "Tên nhân viên không được để trống!")
    private String employeeName;

    private Integer gender;

    private Date birthday;

    private String avatar;

    private String phone;

    private String email;

    private String address;

    private Integer isActive;

    private Long departmentId;

    private Long positionId;

}

package com.company_management.dto.response.au;

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
public class ResponseAccountListDTO {
    private Long id;
    private String employeeCode;
    private String departmentName;
    private String positionName;
    private String fullName;
    private String email;
    private Integer status;
    // Vai tro
    private List<AdminRoleDTO> role;
}

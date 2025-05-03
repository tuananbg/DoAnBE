package com.company_management.dto.au;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddRoleDTO {
    private String employeeCode;
    private String email;
    private List<String> roleCodes;
}

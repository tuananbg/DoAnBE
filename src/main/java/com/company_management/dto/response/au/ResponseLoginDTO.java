package com.company_management.dto.response.au;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLoginDTO {
    private String token;

    private String fullName;

    private String employeeCode;

    private String roles;

    private String email;
}

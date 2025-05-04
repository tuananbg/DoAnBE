package com.company_management.dto.response.au;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLoginDTO {
    private String token;

    private String fullName;

    private String employeeCode;

    private List<String> roles;

    private String email;
}

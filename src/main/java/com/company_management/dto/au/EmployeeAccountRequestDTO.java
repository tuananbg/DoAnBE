package com.company_management.dto.au;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAccountRequestDTO implements Serializable {

    @NotNull
    private String code;

    @NotNull
    private String email;
}

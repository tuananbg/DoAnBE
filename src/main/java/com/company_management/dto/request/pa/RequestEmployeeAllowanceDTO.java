package com.company_management.dto.request.pa;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmployeeAllowanceDTO {
    @NotNull(message = "Không được để trống mã hợp đồng")
    private String  allowanceCode;
    @NotNull(message = "Không được để trống mã nhân viên")
    private String employeeCode;
}

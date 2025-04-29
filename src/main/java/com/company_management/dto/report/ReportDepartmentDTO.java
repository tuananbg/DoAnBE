package com.company_management.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDepartmentDTO {
    private String departmentCode;
    private String departmentName;
    private int totalEmployee;
    private String status;
}

package com.company_management.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportEmployeeContractDTO {
    private String employeeCode;
    private String fullName;
    private String contractNumber;
    private String contractType;
    private String description;
    private Date contractSignDate;
    private Date contractEffectiveDate;
    private Date contractEndDate;
    private BigDecimal salaryRate;
    private BigDecimal basicSalaryInsurance;
    private BigDecimal basicSalary;
    private String statusName;
}

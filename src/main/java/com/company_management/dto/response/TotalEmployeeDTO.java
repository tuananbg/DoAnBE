package com.company_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalEmployeeDTO {
    private int totalEmployee;
    private Long totalBirthDayMonth;
    private int totalLateWork;
    private int totalLeaveWork;
}

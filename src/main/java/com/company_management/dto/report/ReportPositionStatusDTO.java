package com.company_management.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportPositionStatusDTO {

    private String positionCode;

    private String positionName;

    private String positionDescription;

    private String departmentCode;

    private String departmentName;

    private String positionCategoryName;

    private String jobGroupName;

    private String status;

    private String totalEmployee;
}

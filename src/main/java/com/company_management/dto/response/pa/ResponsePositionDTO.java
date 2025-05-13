package com.company_management.dto.response.pa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePositionDTO {
    private Long id;

    private String positionName;

    private String positionCode;

    private String positionDescription;

    private String departmentName;

    private String departmentCode;

    private String positionCategoryName;

    private String positionCategoryCode;

    private String jobGroupName;

    private String jobGroupCode;

    private Integer status;
}

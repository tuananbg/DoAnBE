package com.company_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePositionDTO {

    private String positionName;

    private String positionCode;
    private String positionDescription;

    private String departmentName;

    private Integer isActive;
}

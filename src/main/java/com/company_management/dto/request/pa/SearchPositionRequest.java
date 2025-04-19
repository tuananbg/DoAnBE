package com.company_management.dto.request.pa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPositionRequest {

    private Integer positionCode;
    private String positionName;
    private Integer status;


}

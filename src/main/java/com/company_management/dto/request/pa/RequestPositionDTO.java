package com.company_management.dto.request.pa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestPositionDTO {
    private Long id;

    //Tên chức danh
    private String positionName;

    //Mã chức danh
    private String positionCode;

    //Mô tả
    private String positionDescription;

    //Đơn vị
    private String departmentCode;

    //Loại chức danh
    private String positionCategory;

    //Cấp bậc
    private String jobGroup;
}

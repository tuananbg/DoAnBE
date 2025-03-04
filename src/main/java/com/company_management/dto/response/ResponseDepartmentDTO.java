package com.company_management.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDepartmentDTO {
    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private Integer status;
}

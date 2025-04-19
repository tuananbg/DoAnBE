package com.company_management.dto.response.pa;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDepartmentDTO {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private Integer status;
}

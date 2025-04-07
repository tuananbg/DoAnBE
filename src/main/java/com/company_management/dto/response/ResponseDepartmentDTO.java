package com.company_management.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDepartmentDTO {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private Integer active;
}

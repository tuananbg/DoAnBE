package com.company_management.dto.response.pa;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDepartmentDTO {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private Date createdDate;
    private Integer status;
}

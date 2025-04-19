package com.company_management.dto.request.attendace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchLeaveRequest {

    private Date startDay;
    private Date endDay;
    private Integer status;
    private Long employeeId;

}

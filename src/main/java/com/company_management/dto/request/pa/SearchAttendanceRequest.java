package com.company_management.dto.request.pa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAttendanceRequest {
    private Date workingDay;
}

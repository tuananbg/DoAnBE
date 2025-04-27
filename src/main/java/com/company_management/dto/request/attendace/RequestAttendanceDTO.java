package com.company_management.dto.request.attendace;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAttendanceDTO {
    private long id;

    private String employeeCode;
}

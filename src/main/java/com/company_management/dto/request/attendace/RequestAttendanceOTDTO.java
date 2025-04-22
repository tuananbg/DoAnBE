package com.company_management.dto.request.attendace;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAttendanceOTDTO {
    private Date startDay;  // ngày chấm công ot
    private Date startTime; //thời gian bắt đầu
    private Date endTime; //thời gian kết thúc
    private Double totalTime;
    private String employeeCode;
    private String followCode;
    private String descriptionOt;  //mô tả công việc cần ot
}

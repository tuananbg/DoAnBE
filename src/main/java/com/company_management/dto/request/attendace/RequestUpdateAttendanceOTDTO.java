package com.company_management.dto.request.attendace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateAttendanceOTDTO {
    private long id;
    private Date startDay;  // ngày chấm công ot
    private Date startTime; //thời gian bắt đầu
    private Date endTime; //thời gian kết thúc
    private Double totalTime;
    private String followCode;
    private String descriptionOt;
    private Integer status;
}

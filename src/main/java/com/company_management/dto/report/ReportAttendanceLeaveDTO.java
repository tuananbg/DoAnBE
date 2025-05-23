package com.company_management.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportAttendanceLeaveDTO {

    private String employeeCode;

    private String employeeName;

    private String positionName;

    private String departmentName;

    private String leaveCategory; // loại nghỉ phép nghỉ tính phép, nghỉ không tính phép)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay; // tu ngày

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  //đến ngày

    //Tổng ngày nghỉ
    private Long totalTime;

    //Nội dung
    private String description;

    private String reviewerCode;

    private String reviewerName;

    private String statusName;
}

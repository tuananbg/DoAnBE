package com.company_management.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportAttendanceOTDTO {
    private String employeeCode;
    private String employeeName;
    private String positionName;
    private String departmentName;
    private Date startDay;  // ngày chấm công ot
    private Date startTime; //thời gian bắt đầu
    private Date endTime; //thời gian kết thúc
    private Double totalTime; //tổng số giờ
    private String descriptionOt;  //mô tả công việc cần ot
    private String followCode;
    private String followName;
    private String status;  // trạng thái đã duyệt, chờ duyệt, từ chối

}

package com.company_management.dto.response.attendance;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAttendanceOTDTO {
    private Long id;
    private Date startDay;  // ngày chấm công ot
    private Date startTime; //thời gian bắt đầu
    private Date endTime; //thời gian kết thúc
    private Double totalTime; //tổng số giờ
    private String employeeCode;
    private String employeeName;
    private String followCode;
    private String followName;
    private String descriptionOt;  //mô tả công việc cần ot
    private Integer status;  // trạng thái đã duyệt, chờ duyệt, từ chối

}

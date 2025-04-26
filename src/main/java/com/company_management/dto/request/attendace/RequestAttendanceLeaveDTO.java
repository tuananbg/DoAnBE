package com.company_management.dto.request.attendace;

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
public class RequestAttendanceLeaveDTO {
    // loại nghỉ phép nghỉ tính phép, nghỉ không tính phép)
    private String leaveCategory;
    private String employeeCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay; // tu ngày
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  //đến ngày
    private String description; //Nội dung
    private Long totalTime;  //Tổng số ngày
    private String reviewerCode;
    private Integer status;  // trạng thái đã duyệt, chờ duyệt, từ chối
}

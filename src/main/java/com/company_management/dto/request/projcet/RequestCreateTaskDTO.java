package com.company_management.dto.request.projcet;

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
public class RequestCreateTaskDTO {

    private String taskName;  // ten cong viec

    private String taskCode;  // ma cong viec

    private String taskDescription; // mô ta cong viec

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay; // ngay bat dau

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  // han ket thuc

    //Dự án
    private String projectCode;

    //Nhân viên phụ trách
    private String employeeCode;

    private String managerCode;

    private int priority;  //do uu tien
}

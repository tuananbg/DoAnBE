package com.company_management.dto.response.project;

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
public class ResponseDetailTaskDTO {
    private long id;

    private String taskName;  // ten cong viec

    private String taskCode;  // ma cong viec

    private String taskDescription; // mô ta cong viec

    private String managerCode; // Người theo dõi

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay; // ngay bat dau

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  // han ket thuc


    private String projectCode;

    private String employeeCode;

    private int priority;  //do uu tien
}

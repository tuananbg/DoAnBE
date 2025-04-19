package com.company_management.dto.request.projcet;

import com.company_management.entity.Employee;
import com.company_management.entity.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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
    private Project project;

    //Nhân viên phụ trách
    private Employee employee;

    private int priority;  //do uu tien
}

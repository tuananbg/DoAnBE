package com.company_management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ResponseListProjectDTO {
    private Long id;

    private String projectName;  //ten du an

    private String projectDescription; // mo ta du an

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay;  // ngay bat dau

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  // han ket thuc

    private Double estimate;  // so gio du kien

    private Double timesheet;  // so gio thuc hien dang đc khai bao

    //Số lượng nhiệm vụ hiện tại
    private int taskNumber;
}

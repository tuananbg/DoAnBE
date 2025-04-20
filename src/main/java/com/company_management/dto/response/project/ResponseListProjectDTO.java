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
public class ResponseListProjectDTO {
    private Long id;

    private String projectName;  //ten du an

    private String projectDescription; // mo ta du an

    private String projectStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay;  // ngay bat dau

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  // han ket thuc

    // Số lượng nhiệm vụ hoàn thành
    private String taskDone;

    // Số lượng nhiệm vụ đang làm
    private String taskTodo;

    //Số lượng nhiệm vụ đang xử lý
    private String taskProcess;
}

package com.company_management.entity;

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
@Entity
@Table(name = "ATTENDANCE_LEAVE") // bảng xin nghỉ phép
public class AttendanceLeave extends BaseEntity {

    @Column(name = "LEAVE_CATEGORY")
    private String leaveCategory; // loại nghỉ phép nghỉ tính phép, nghỉ không tính phép)

    @Column(name = "START_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay; // tu ngày

    @Column(name = "END_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  //đến ngày

    @Column(name = "TOTAL_TIME")
    private Long totalTime;  //số giờ

    @Column(name = "DESCRIPTION")
    private String description; //Nội dung

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "id")
    private Employee employee;

    @Column(name = "REVIEWER_ID")
    private Long reviewerId;  //người phe duyệt

}

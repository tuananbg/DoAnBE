package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ATTENDANCE_OT") // bảng chấm công ot
public class AttendanceOt extends BaseEntity {

    @Basic
    @Column(name = "START_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay;  // ngày chấm công ot

    @Basic
    @Column(name = "START_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startTime; //thời gian bắt đầu

    @Basic
    @Column(name = "END_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endTime; //thời gian kết thúc

    @Basic
    @Column(name = "TOTAL_TIME")
    private Double totalTime; //tổng số giờ

    @Basic
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Basic
    @Column(name = "FOLLOW_ID")
    private Long followId;  // người theo dõi

    @Basic
    @Column(name = "DESCRIPTION_OT")
    private String descriptionOt;  //mô tả công việc cần ot

}

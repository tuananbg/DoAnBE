package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "START_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay;  // ngày chấm công ot

    @Column(name = "START_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startTime; //thời gian bắt đầu

    @Column(name = "END_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endTime; //thời gian kết thúc

    @Column(name = "TOTAL_TIME")
    private Double totalTime; //tổng số giờ

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "id")
    private Employee employee;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOW_ID", referencedColumnName = "id")
    private Employee employeeFollow;  // người theo dõi

    @Column(name = "DESCRIPTION_OT")
    private String descriptionOt;  //mô tả công việc cần ot

    @Column(name = "STATUS")
    private Integer status ;
}

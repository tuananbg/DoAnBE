package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ATTENDANCE") // bảng chấm công
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Attendance extends BaseEntity {
    //mã nhân viên
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    //ngày làm
    @Column(name = "WORKING_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date workingDay;

    //đăng ký giờ vào
    @Column(name = "CHECK_IN_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date checkInTime;

    //đăng ky giờ ra
    @Column(name = "CHECK_OUT_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date checkOutTime;

    //sô giờ
    @Column(name = "WORKING_TIME")
    private Double workingTime;

    //hệ số công
    @Column(name = "WORKING_POINT")
    private Double workingPoint;

    //tổng phút phạt
    @Column(name = "TOTAL_PENALTY")
    private Long totalPenalty;

}

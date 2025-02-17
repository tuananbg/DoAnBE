package com.company_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ON_LEAVE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Leave extends BaseEntity {


    @Column(name = "LEAVE_CATEGORY", nullable = false)
    private String leaveCategory;

    @Column(name = "START_DAY", nullable = false)
    private Date startDay;

    @Column(name = "END_DAY")
    private Date endDay;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "TOTAL_TIME")
    private Long totalTime;

    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "TRACKER_ID")
    private Long trackerId;

    @Column(name = "REVIEWER_ID")
    private Long reviewerId;

}

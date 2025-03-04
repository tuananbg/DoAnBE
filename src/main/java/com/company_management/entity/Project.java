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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROJECT")  // bang du an cong ty
public class Project extends BaseEntity {

    @Basic
    @Column(name = "PROJECT_NAME")
    private String projectName;  //ten du an

    @Basic
    @Column(name = "PROJECT_DESCRIPTION")
    private String projectDescription; // mo ta du an

    @Basic
    @Column(name = "START_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay;  // ngay bat dau

    @Basic
    @Column(name = "END_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  // han ket thuc

    @Basic
    @Column(name = "ESTIMATE")
    private Double estimate;  // so gio du kien

    @Basic
    @Column(name = "TIMESHEET")
    private Double timesheet;  // so gio thuc hien dang đc khai bao

    @Basic
    @Column(name = "TASK_NUMBER")
    private Long taskNumber;  // so luong task

}

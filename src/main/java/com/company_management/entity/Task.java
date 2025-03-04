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
@Table(name = "TASK")  // bang cong viec
public class Task extends BaseEntity {

    @Column(name = "TASK_NAME")
    private String taskName;  // ten cong viec

    @Column(name = "TASK_CODE")
    private String taskCode;  // ma cong viec

    @Column(name = "TASK_DESCRIPTION")
    private String taskDescription; // mô ta cong viec

    @Column(name = "START_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay; // ngay bat dau

    @Column(name = "END_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  // han ket thuc

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    private Project project;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOW_ID", referencedColumnName = "id")
    private Employee employee;

    @Column(name = "PRIORITY")
    private int priority;  //do uu tien

}

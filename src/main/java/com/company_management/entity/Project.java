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
@Table(name = "PROJECT")  // bang du an cong ty
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Basic
    @Column(name = "PROJECT_NAME")
    private String projectName;  //ten du an

    @Basic
    @Column(name = "PROJECT_DESCRIPTION")
    private String projectDescription; // mo ta du an

    @Basic
    @Column(name = "PROJECT_MANAGER_CODE")
    private String projectManagerCode;  //Quản lý dự án

    @Basic
    @Column(name = "CLIENT_NAME")
    private String clientName; // Tên khách hàng

    @Basic
    @Column(name = "START_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDay;  // ngay bat dau

    @Basic
    @Column(name = "END_DAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date endDay;  // han ket thuc

    @Column(name = "STATUS")
    private Integer status ;
}

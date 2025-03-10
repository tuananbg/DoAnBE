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
@Table(name = "SOCIAL_INSURANCE")  //thông tin bảo hiểm
public class SocialInsurance extends BaseEntity {

    @Column(name = "SOCIAL_INSURANCE_CODE")
    private String socialInsuranceCode; // ma so BHXH

    @Column(name = "INITIAL_PAYMENT")
    private Double initialPayment; // Mức đóng ban đầu

    @Column(name = "PERCENT")
    private Double percent; // Giảm số phần trăm

    @Column(name = "ACTUAL_PAYMENT")
    private Double actualPayment; // Thực tế nhân viên đóng

    @Column(name = "PLACEMENT")
    private String placement; // noi kham chua benh

    @Column(name = "LICENSE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date licenseDate; // ngay hieu luc

    @Column(name = "EXPIRED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date expiredDate; // ngay het han

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "id")
    private Employee employee;
}

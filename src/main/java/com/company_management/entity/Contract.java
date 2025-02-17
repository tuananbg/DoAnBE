package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "CONTRACT")  //Hợp đồng
public class Contract extends BaseEntity {

    @Column(name = "CONTRACT_CODE")
    private String contractCode;

    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    @Column(name = "USER_DETAIL_ID")
    private Long userDetailId;

    @Column(name = "ACTIVE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date activeDate;

    @Column(name = "EXPIRED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date expiredDate;

    @Column(name = "SIGN_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date signDate;

    @Column(name = "ATTACH_FILE")
    private String attachFile; // Lưu đường dẫn file hoặc tên file
}

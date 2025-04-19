package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "account")
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Account extends BaseEntity {

    //    @Schema(description = "Tên đăng nhập (duy nhất)")
    @Column(name = "ACCOUNT")
    private String account;

    //Mã CBNV
    @Column(name = "code")
    private String code;

    @JsonIgnore
//    @Schema(description = "Mật khẩu")
    @Column(name = "PASSWORD")
    private String password;


//    @Schema(description = "Trạng thái của user")
    @Column(name = "STATUS")
    private Integer status;

//    @Schema(description = "Ngày hết hạn mật khẩu")
    @Column(name = "PW_EXP_DATE")
    private Date pwExpDate;

//    @Schema(description = "số lần nhập sai mật khẩu")
    @Column(name = "NUM_PW_WRONG")
    private Integer numPwWrong;

//    @Schema(description = "OTP")
    @Column(name = "OTP")
    private String otp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "id")
    private Employee employee;

}

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT")
    private String account;

    @Column(name = "code")
    private String code;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "PW_EXP_DATE")
    private Date pwExpDate;

    @Column(name = "NUM_PW_WRONG")
    private Integer numPwWrong;

    @Column(name = "OTP")
    private String otp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "id")
    private Employee employee;

}

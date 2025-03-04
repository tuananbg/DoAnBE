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
@Table(name = "EMPLOYEE_CONTRACTS")  //Hợp đồng
public class EmployeeContracts extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @Column(name = "EMPLOYEE_CODE") // Mã CBNV
    private String employeeCode;

    @Column(name = "EMPLOYEE_NAME") // Tên CBNV
    private String employeeName;

    @Column(name = "CONTRACT_TYPE") // ID loai HD
    private String contractType;

    @Column(name = "CONTRACT_TYPE_DISPLAY") // Loại HD
    private String contractTypeDisplay;

    @Column(name = "CONTRACT_NUMBER") // Số HD
    private String contractNumber;

    @Column(name = "CONTRACT_SIGN_DATE") // Ngày ký
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date contractSignDate;

    @Column(name = "CONTRACT_EFFECTIVE_DATE") // Ngày hiệu lực
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date contractEffectiveDate;

    @Column(name = "CONTRACT_END_DATE") // Ngày hết hiệu lực
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date contractEndDate;

    @Column(name = "CONTRACT_TERM") // Thời hạn HD
    private Integer contractTerm;

    @Column(name = "CONTRACT_TERM_VALUE") // Giá trị thời hạn
    private String contractTermValue;

    @Column(name = "CONTRACT_TERM_DISPLAY") // Thời hạn HD hiển thị
    private String contractTermDisplay;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SALARY_RATE") // Tỷ lệ hưởng lương
    private Float salaryRate;

    @Column(name = "BASIC_SALARY_INSURANCE") // Lương cơ bản tham gia bảo hiểm
    private Double basicSalaryInsurance;

    @Column(name = "APPLY_BUSINESS_SALARY") // Áp dụng lương kinh doanh
    private String applyBusinessSalary;

    @Column(name = "BASIC_SALARY") // Lương cơ bản khi tạo mới nhân viên
    private Long basicSalary;

    @Column(name = "ATTACH_FILE")
    private String attachFile; // Lưu đường dẫn file hoặc tên file


}

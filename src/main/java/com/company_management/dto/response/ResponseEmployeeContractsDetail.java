package com.company_management.dto.response;

import com.company_management.entity.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ResponseEmployeeContractsDetail {

    // Mã CBNV
    private String employeeCode;

    // Tên CBNV
    private String employeeName;

    // ID loai HD
    private String contractType;

   // Loại HD
    private String contractTypeDisplay;

    // Số HD
    private String contractNumber;

    // Ngày ký
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date contractSignDate;

    // Ngày hiệu lực
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date contractEffectiveDate;

    // Ngày hết hiệu lực
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date contractEndDate;

    // Thời hạn HD
    private Integer contractTerm;

    // Giá trị thời hạn
    private String contractTermValue;

    // Thời hạn HD hiển thị
    private String contractTermDisplay;


    private String description;

    // Tỷ lệ hưởng lương
    private Float salaryRate;

    // Lương cơ bản tham gia bảo hiểm
    private Double basicSalaryInsurance;

   // Áp dụng lương kinh doanh
    private String applyBusinessSalary;

    // Lương cơ bản khi tạo mới nhân viên
    private Long basicSalary;

    private String attachFile; // Lưu đường dẫn file hoặc tên file

}

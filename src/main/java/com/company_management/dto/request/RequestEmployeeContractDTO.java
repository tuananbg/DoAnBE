package com.company_management.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmployeeContractDTO {
   // Mã CBNV
    private String employeeCode;

    // ID loai HD
    private String contractType;

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

    private String description;

    // Tỷ lệ hưởng lương
    private Float salaryRate;

    // Lương cơ bản tham gia bảo hiểm
    private Double basicSalaryInsurance;

    // Lương cơ bản khi tạo mới nhân viên
    private Long basicSalary;

    private String attachFile; // Lưu đường dẫn file hoặc tên file
}

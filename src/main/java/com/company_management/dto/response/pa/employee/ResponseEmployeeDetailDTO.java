package com.company_management.dto.response.pa.employee;


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
public class ResponseEmployeeDetailDTO {

    private Long id;

    //Mã CBNV
    private String employeeCode;

    //Họ tên
    private String employeeName;

    //Ảnh đại diện
    private String avatar;

    //Phòng ban
    private String departmentName;

    //Chức vụ
    private String positionName;

    //Ngày sinh
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date birthDay;

    //Giới tính
    private int gender;

    //Nơi sinh
    private String placeOfBirth;

    //Mã số thuế
    private String taxCode;

    //Số bảo hiểm
    private String insuranceNumber;

    //Địa chỉ thường trú
    private String permanentAddress;

    //Địa chỉ hiện tại
    private String currentAddress;

    //
    private String identityNumber;

    //Só điện thoại
    private String mobile;

    private String email;

    //
    private String nation;
}

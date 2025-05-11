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
    private String code;

    //Họ tên
    private String fullName;

    //Ảnh đại diện
    private String avatar;

    //Phòng ban
    private String positionCode;



    //Chức vụ
    private String positionName;

    //Ngày sinh
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date dateOfBirth;

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


    private String identityNumber;

    private String accountNumber;

    //Só điện thoại
    private String mobile;

    private String email;

}

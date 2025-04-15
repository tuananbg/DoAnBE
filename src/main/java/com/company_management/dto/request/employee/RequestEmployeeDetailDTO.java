package com.company_management.dto.request.employee;


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
public class RequestEmployeeDetailDTO {

    private String code;

    private String fullName;

    private String avatar;

    private String departmentCode;

    private String positionCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date dateOfBirth;

    private int gender;

    private String placeOfBirth;

    private String taxCode;

    private String insuranceNumber;

    private String accountNumber;

    private String permanentAddress;

    private String currentAddress;

    private String identityNumber;

    private String mobile;

    private String nation;

}

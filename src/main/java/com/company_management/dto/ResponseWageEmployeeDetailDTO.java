package com.company_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWageEmployeeDetailDTO {

    private String wageName;

    private Double wageBase; //luong phu cap co ban( số tien)

    private String wageDescription;

    private String empSign;

    private Date licenseDate;

    private String attachFile;  //file thong tin phu cap
}

package com.company_management.dto.response.pa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAllowanceEmployeeDetailDTO {

    private String allowanceCode;

    private String employeeCode;

    private String allowanceName;

    private Double allowanceBase; //luong phu cap co ban( số tien)

    private String allowanceDescription;

    private String attachFile;  //file thong tin phu cap
}

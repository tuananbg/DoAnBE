package com.company_management.dto.response.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEmployeeDetailWageDTO {

    private Long wageId;

    private String wageName;

    private Double wageBase; //so tiền phu cap

    private String wageDescription;

    private String attachFile;  //file thong tin phu cap

    private String empSign;

    private Date licenseDate;

    private Date createdDate;
}

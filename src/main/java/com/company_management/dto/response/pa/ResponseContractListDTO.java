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
public class ResponseContractListDTO {
    private long id;
    private String employeeCode;
    private String employeeName;
    private String contractNumber; // ma hop dong
    private String contractType; // loai hop dong: thu viec, chinh thuc,....
    private String attachFile;
    private Date signDate; // ngay ky HD
    private Date effectiveDate; // ngay hieu luc
    private Date expiredDate; // thoi han hop dong
    private String contractTerm;
    private String contractStatus; // la hop dong hien tai

}

package com.company_management.dto.response;

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
    private String employeeCode;
    private String employeeName;
    private Long contractId;
    private String contractCode; // ma hop dong
    private String contractType; // loai hop dong: thu viec, chinh thuc,....
    private String attachFile;
    private Date signDate; // ngay ky HD
    private Date activeDate; // ngay hieu luc
    private Date expiredDate; // thoi han hop dong
    private Integer isActive; // la hop dong hien tai

}

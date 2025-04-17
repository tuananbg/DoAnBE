package com.company_management.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestSocialInsuranceDTO {
    private String socialInsuranceCode; // ma so BHXH
    private Double initialPayment;  //mức dong ban dau
    private Double percent;
    private Double actualPayment;  // mức dong thuc te
    private String placement; // noi kham chua benh
    private Date licenseDate; // ngay hieu luc
    private Date expiredDate; // ngay het han
    private String employeeCode;

}

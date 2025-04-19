package com.company_management.dto.response.pa.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEmployeeDetailSocialDTO {

    private Long socialInsuranceId;

    private String socialInsuranceCode; // ma so BHXH

    private Double initialPayment;  //mức dong ban dau

    private Double percent;

    private Double actualPayment;  // mức dong thuc te

    private String placement; // noi kham chua benh

    private Date licenseDate; // ngay hieu luc

    private Date expiredDate; // ngay het han

}

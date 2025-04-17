package com.company_management.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestQualificationDTO {

    //loại, trình độ bằng cấp (, cử nhân, thạc sĩ, tiến sĩ, )
    private String level;

    //tên bằng cấp
    private String name;

    // chuyên ngành
    private String major;

    //mô tả
    private String description;

    //ngày cấp bằng
    private Date licenseDate;

    //Mã CBNV
    private String employeeCode;

}

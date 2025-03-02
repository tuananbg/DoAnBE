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
public class ResponseQualificationEmployeeDetailDTO {

    private int index;

    private String level;  //loại, trình độ bằng cấp (, cử nhân, thạc sĩ, tiến sĩ, )

    private String name; //tên bằng cấp

    private String major; // chuyên ngành

    private String description;  //mô tả

    private Date licenseDate; //ngày cấp bằng

}

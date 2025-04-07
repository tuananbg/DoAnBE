package com.company_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSeatDTO {

    //Mã vị trí
    private String code;

    //Tên chức danh
    private String positionName;

    //Tên phòng ban
    private String departmentName;

    private String description;

    private Boolean active;

}

package com.company_management.dto.request.pa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestSeatDTO {
    //Mã ghế
    private String code;

    //Mô tả
    private String description;

    private Long positionId;

}

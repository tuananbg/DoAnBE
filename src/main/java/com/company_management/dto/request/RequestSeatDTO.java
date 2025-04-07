package com.company_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestSeatDTO {
    private String code;

    private Long directManager;

    private String description;

    private Long positionId;

}

package com.company_management.dto.request.pa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAllowanceCreateDTO {

    private Long wageId;

    private String allowanceCode;

    private String allowanceName;

    private Double allowanceBase;

    private String allowanceDescription;

    private String attachFile;

}

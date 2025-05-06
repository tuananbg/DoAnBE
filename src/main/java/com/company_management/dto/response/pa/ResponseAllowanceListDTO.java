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
public class ResponseAllowanceListDTO {
    private int index;
    private Long wageId;
    private String allowanceCode;
    private String allowanceName;
    private Double allowanceBase; //so tiền phu cap
    private String allowanceDescription;
    private Integer status;
    private String attachFile;  //file thong tin phu cap
    private Date createdDate;
}

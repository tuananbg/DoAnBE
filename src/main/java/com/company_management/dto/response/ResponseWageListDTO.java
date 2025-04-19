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
public class ResponseWageListDTO {
    private int index;
    private Long wageId;
    private String wageName;
    private Double wageBase; //so tiền phu cap
    private String wageDescription;
    private Integer status;
    private String attachFile;  //file thong tin phu cap
    private Date createdDate;
}

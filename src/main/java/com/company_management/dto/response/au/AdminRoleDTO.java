package com.company_management.dto.response.au;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleDTO {
    private String code;


    private String name;


    private Boolean active;


    private Boolean isMaster;


    private String description;
}

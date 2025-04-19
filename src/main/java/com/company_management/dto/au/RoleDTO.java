package com.company_management.dto.au;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean active;
    private int positionCount;
    private int employeeCount;

    @JsonProperty("isMaster")
    private Boolean isMaster;
}

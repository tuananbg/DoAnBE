package com.company_management.dto.au;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ConfigDTO {
    private long id;

    @JsonIgnore
    private String code;

    private String value;

    @JsonIgnore
    private int dataType;

    private String groupType;

    private Boolean active;

    private String description;
}

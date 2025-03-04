package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Status {
    INACTIVE(0, "RETIRED", "Không hoạt động"),
    ACTIVE(1, "EMPLOYMENT", "Hoạt động"),
    LOCK(2,"LOCK","Account đang bị khoá"),
    ;

    @JsonValue
    private Integer code;

    private String status;

    private String description;

    public static Status findByCode(String status) {
        for (Status status1 : values()) {
            if (status1.getStatus().equalsIgnoreCase(status)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("status = " + status + " isn't defined!!!");
    }

    public static Status findByCodeStatus(Integer code) {
        for (Status status1 : values()) {
            if (status1.getCode().equals(code)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("code = " + code + " isn't defined!!!");
    }

}

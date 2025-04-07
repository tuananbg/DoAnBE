package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ObjectStatus {
    INACTIVE(0, "INACTIVE", "Không hoạt động"),
    ACTIVE(1, "ACTIVE", "Hoạt động"),
    ;

    @JsonValue
    private Integer code;

    private String status;

    private String description;

    public static ObjectStatus findByCode(String status) {
        for (ObjectStatus status1 : values()) {
            if (status1.getStatus().equalsIgnoreCase(status)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("status = " + status + " isn't defined!!!");
    }

    public static ObjectStatus findByCodeStatus(Integer code) {
        for (ObjectStatus status1 : values()) {
            if (status1.getCode().equals(code)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("code = " + code + " isn't defined!!!");
    }

}

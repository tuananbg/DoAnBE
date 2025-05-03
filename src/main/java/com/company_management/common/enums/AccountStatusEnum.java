package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AccountStatusEnum {
    ACTIVE(1, "ACTIVE", "Hoạt động"),
    LOCK(2, "LOCK", "Khóa"),
    ;

    @JsonValue
    private Integer code;

    private String status;

    private String description;

    public static AccountStatusEnum findByCode(String status) {
        for (AccountStatusEnum status1 : values()) {
            if (status1.getStatus().equalsIgnoreCase(status)) {
                return status1;
            }
        }
        return null;
    }

    public static AccountStatusEnum findByCodeStatus(Integer code) {
        for (AccountStatusEnum status1 : values()) {
            if (status1.getCode().equals(code)) {
                return status1;
            }
        }
        return null;
    }
}

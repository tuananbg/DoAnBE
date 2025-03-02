package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DepartmentStatus {
    INACTIVE(0, "INACTIVE", "Không hoạt động"),
    ACTIVE(1, "ACTIVE", "Hoạt động"),
    ;

    @JsonValue
    private Integer code;

    private String status;

    private String description;

    public static DepartmentStatus findByCode(String status) {
        for (DepartmentStatus departmentStatus : values()) {
            if (departmentStatus.getStatus().equalsIgnoreCase(status)) {
                return departmentStatus;
            }
        }
        throw new IllegalArgumentException("status = " + status + " isn't defined!!!");
    }

    public static DepartmentStatus findByCodeStatus(Integer code) {
        for (DepartmentStatus departmentStatus : values()) {
            if (departmentStatus.getCode().equals(code)) {
                return departmentStatus;
            }
        }
        throw new IllegalArgumentException("code = " + code + " isn't defined!!!");
    }

    public static DepartmentStatus fromString(final String s) {
        return findByCode(s);
    }

}

package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum EmploymentStatus {
    RETIRED(0, "RETIRED", "Nghỉ việc"),
    EMPLOYMENT(1, "EMPLOYMENT", "Đang làm việc"),
    LOCK(2,"LOCK","Account đang bị khoá"),
    ;

    @JsonValue
    private Integer code;

    private String status;

    private String description;

    public static EmploymentStatus findByCode(String status) {
        for (EmploymentStatus employeeStatus : values()) {
            if (employeeStatus.getStatus().equalsIgnoreCase(status)) {
                return employeeStatus;
            }
        }
        throw new IllegalArgumentException("status = " + status + " isn't defined!!!");
    }

    public static EmploymentStatus findByCodeStatus(Integer code) {
        for (EmploymentStatus employeeStatus : values()) {
            if (employeeStatus.getCode().equals(code)) {
                return employeeStatus;
            }
        }
        throw new IllegalArgumentException("code = " + code + " isn't defined!!!");
    }

    public static EmploymentStatus fromString(final String s) {
        return findByCode(s);
    }

}

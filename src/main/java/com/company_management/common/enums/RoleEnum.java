package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RoleEnum {
    ADMIN( "ADMIN", "Quản trị viên"),
    USER( "USER", "Nhân viên"),
    MANAGER("MANAGER","Quản lý"),
    ;

    @JsonValue
    private String code;

    private String name;

    public static RoleEnum findByCode(String status) {
        for (RoleEnum role : values()) {
            if (role.getCode().equalsIgnoreCase(status)) {
                return role;
            }
        }
        throw new IllegalArgumentException("status = " + status + " isn't defined!!!");
    }

}

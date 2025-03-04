package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Role {
    ADMIN( "ADMIN", "Không hoạt động"),
    USER( "USER", "Hoạt động"),
    ;

    @JsonValue
    private String code;

    private String name;

    public static Role findByCode(String status) {
        for (Role role : values()) {
            if (role.getCode().equalsIgnoreCase(status)) {
                return role;
            }
        }
        throw new IllegalArgumentException("status = " + status + " isn't defined!!!");
    }

}

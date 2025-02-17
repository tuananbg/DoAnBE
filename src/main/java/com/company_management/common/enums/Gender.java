package com.company_management.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {
    //0 - Issued, 1 - opened, 2-locked, 3 - closed
    ISSUED(0, "Nam"),
    OPENED(1, "Nữ"),
    ;

    private Integer code;
    private String name;

    public static Gender fromCode(final Integer code) {
        return Arrays.stream(values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Code = " + code + " isn't defined!!!"));
    }

    public static Gender fromString(final String name) {
        return Arrays.stream(values())
                .filter(t -> t.getCode().toString().equalsIgnoreCase(name) || t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Name = " + name + " isn't defined!!!"));
    }
}

package com.company_management.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE(0, "Nam"),
    WOMEN(1, "Nữ"),
    OTHER(99,"Khác")
    ;

    private final Integer code;
    private final String name;

    public static Gender fromCode(final Integer code) {
        return Arrays.stream(values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElse(OTHER);
    }

    public static Gender fromString(final String name) {
        return Arrays.stream(values())
                .filter(t -> t.getCode().toString().equalsIgnoreCase(name) || t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(OTHER);
    }
}

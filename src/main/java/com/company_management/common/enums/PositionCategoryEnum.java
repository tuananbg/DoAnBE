package com.company_management.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PositionCategoryEnum {

    DEPARTMENT_HEAD("01", "Trưởng phòng"),
    DEPUTY_HEAD("02", "Phó phòng"),
    SPECIALIST("03","Chuyên viên"),
    STAFF("04","Nhân viên")
    ;

    private final String code;
    private final String name;

    public static PositionCategoryEnum fromCode(final String code) {
        return Arrays.stream(values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElse(STAFF);
    }

    public static PositionCategoryEnum fromString(final String name) {
        return Arrays.stream(values())
                .filter(t -> t.getCode().equalsIgnoreCase(name) || t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(STAFF);
    }
}

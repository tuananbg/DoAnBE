package com.company_management.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ContractType {

    OFFICIAL(1, "01", "Chính thức"),
    APPRENTICESHIP(2, "02", "Học việc"),
    PROBATIONARY(3, "03", "Thử việc");

    private int value;
    private String code;
    private String name;

    public static ContractType from(String value) {
        for (ContractType type : values()) {
            if (type.getName().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    public static ContractType from(final int value) {
        return Arrays.stream(values())
                .filter(t -> t.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status = " + value + " isn't defined!!!"));
    }

    public static ContractType fromCode(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        for (ContractType type : values()) {
            if (type.getCode().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}

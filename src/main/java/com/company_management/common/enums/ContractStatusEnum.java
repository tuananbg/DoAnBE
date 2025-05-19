package com.company_management.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ContractStatusEnum {
    EFFECTIVE(1, "01", "Hiệu lực"),
    ABOUT_TO_EXPIRE(2, "02", "Sắp hết hạn"),
    EXPIRED(3, "03","Hết hạn"),
    TERMINATED(4, "04", "Vô hiệu"),
    NOT_EFFECTIVE(5,"05","Chưa hiệu lực"),
    ;
    private int value;
    private String code;
    private String name;
    public static ContractStatusEnum fromValue(int value) {
        for (ContractStatusEnum mode : ContractStatusEnum.values()) {
            if (mode.getValue() == value) {
                return mode;
            }
        }
        return null;
    }

    public static ContractStatusEnum fromCode(final String code) {
        if (code != null) {
            return Arrays.stream(values())
                    .filter(t -> t.getCode().equalsIgnoreCase(code))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("code = " + code + " isn't defined!!!"));
        }
        return null;
    }
}

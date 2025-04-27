package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AttendanceStatusEnum {
    CHECKIN(1, "CHECKIN", "Vào làm"),
    CHECKOUT(2, "CHECKOUT", "Ra về"),
    ;

    @JsonValue
    private Integer value;

    private String code;

    private String description;

    public static AttendanceStatusEnum findByCode(String name) {

        return Arrays.stream(values())
                .filter(t -> t.getCode().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static AttendanceStatusEnum findByValue(Integer value) {
        return Arrays.stream(values())
                .filter(t -> t.getValue() == value)
                .findFirst()
                .orElse(null);
    }
}

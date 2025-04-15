package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum TimeUnit {

    DAY(1, "DAY", "Unit of time is Day", "Ngày"),
    MONTH(2, "MONTH", "Unit of time is Month", "Tháng"),
    YEAR(3, "YEAR", "Unit of time is Year", "Năm"),
    ;

    @JsonValue
    private int value;
    private String type;
    private String description;
    private String vneseType;

    public static TimeUnit fromValue(int value) {
        for (TimeUnit type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }


    public static TimeUnit fromCode(final String code) {
        try {
            // Kiểm tra nếu code là kiểu số
            // Nếu parse thành công, code là kiểu số, dùng fromString1
            return fromValue(Integer.parseInt(code));
        } catch (NumberFormatException e) {
            // Nếu gặp lỗi khi parse, code là kiểu chữ, dùng fromString
            return fromString(code);
        }
    }

    public static TimeUnit fromString(final String code) {
        if (code != null) {
            return Arrays.stream(values())
                    .filter(t -> t.getType().equalsIgnoreCase(code))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("status = " + code + " isn't defined!!!"));
        }
        return null;
    }
}
package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum TableTabType {
    TODO(1, "TODO", "Chờ duyệt"),
    REJECT(2, "REJECT", "Từ chối"),
    DONE(3, "DONE", "Đã duyệt"),
    ;

    @JsonValue
    private Integer code;

    private String name;

    private String description;

    public static TableTabType findByCode(String name) {
        for (TableTabType status1 : values()) {
            if (status1.getName().equalsIgnoreCase(name)) {
                return status1;
            }
        }
        return null;
    }

    public static TableTabType findByCode(Integer code) {
        for (TableTabType status1 : values()) {
            if (status1.getCode().equals(code)) {
                return status1;
            }
        }
        return null;
    }
}

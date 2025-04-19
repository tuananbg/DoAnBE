package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ProjectStatus {
    TODO(1, "TODO", "Sắp khởi động"),
    PROCESSING(2, "PROCESSING", "Đang thực hiện"),
    DONE(3, "DONE", "Hoàn thành"),
    ;

    @JsonValue
    private Integer code;

    private String name;

    private String description;

    public static ProjectStatus findByCode(String name) {
        for (ProjectStatus status1 : values()) {
            if (status1.getName().equalsIgnoreCase(name)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("status = " + name + " isn't defined!!!");
    }

    public static ProjectStatus findByCode(Integer code) {
        for (ProjectStatus status1 : values()) {
            if (status1.getCode().equals(code)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("code = " + code + " isn't defined!!!");
    }
}

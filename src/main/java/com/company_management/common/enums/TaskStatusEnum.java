package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TaskStatusEnum {
    TODO(1, "TODO", "Chưa làm"),
    PROCESSING(2, "PROCESSING", "Đang xử lý"),
    DONE(3, "DONE", "Hoàn thành"),
    ;

    @JsonValue
    private Integer code;

    private String name;

    private String description;

    public static TaskStatusEnum findByCode(String name) {
        for (TaskStatusEnum status1 : values()) {
            if (status1.getName().equalsIgnoreCase(name)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("status = " + name + " isn't defined!!!");
    }

    public static TaskStatusEnum findByCode(Integer code) {
        for (TaskStatusEnum status1 : values()) {
            if (status1.getCode().equals(code)) {
                return status1;
            }
        }
        throw new IllegalArgumentException("code = " + code + " isn't defined!!!");
    }
}

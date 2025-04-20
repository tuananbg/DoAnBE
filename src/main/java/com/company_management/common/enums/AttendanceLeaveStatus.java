package com.company_management.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AttendanceLeaveStatus {
    TODO(1, "TODO", "Chờ xử lý"),
    PROCESSING(2, "PROCESSING", "Đang phê duyệt"),
    DONE(3, "DONE", "Chấp thuận"),
    ;

    @JsonValue
    private Integer code;

    private String name;

    private String description;

    public static AttendanceLeaveStatus findByCode(String name) {
        for (AttendanceLeaveStatus status1 : values()) {
            if (status1.getName().equalsIgnoreCase(name)) {
                return status1;
            }
        }
        return null;
    }

    public static AttendanceLeaveStatus findByCode(Integer code) {
        for (AttendanceLeaveStatus status1 : values()) {
            if (status1.getCode().equals(code)) {
                return status1;
            }
        }
        return null;
    }
}

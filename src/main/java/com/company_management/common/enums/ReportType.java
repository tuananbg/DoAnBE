package com.company_management.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ReportType {
    PDF("pdf", "PDF"),
    XLSX("xlsx", "XLSX");

    private String code;

    private String description;

    public static ReportType findByCode(String code) {
        return Arrays.stream(values())
                .filter(t -> t.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("status = " + code + " isn't defined!!!"));
    }
}
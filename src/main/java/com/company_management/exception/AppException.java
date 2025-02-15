package com.company_management.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {

    private final String code;

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }
}

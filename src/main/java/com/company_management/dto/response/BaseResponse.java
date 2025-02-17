package com.company_management.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

    private String code;

    private String message;

    private T data;

    /**
     * Using for save history in db
     */
    @JsonIgnore
    private String historyContent;

    public static BaseResponse<?> success(String historyContent) {
        return ok("COMMON_OK", "success", null, historyContent);
    }

    public static BaseResponse<?> ok() {
        return ok(null);
    }

    public static <T> BaseResponse<T> ok(T data) {
        return ok("COMMON_OK", "success", data);
    }

    public static <T> BaseResponse<T> ok(T data, String historyContent) {
        return ok("COMMON_OK", "success", data, historyContent);
    }

    public static <T> BaseResponse<T> ok(String code, String messsage) {
        return new BaseResponse<>(code, messsage, null, null);
    }

    public static <T> BaseResponse<T> ok(String code, String messsage, T data) {
        return new BaseResponse<>(code, messsage, data, null);
    }

    public static <T> BaseResponse<T> ok(String code, String messsage, T data, String historyContent) {
        return new BaseResponse<>(code, messsage, data, historyContent);
    }

    public static BaseResponse<?> error(String messsage) {
        return error(messsage, null);
    }

    public static <T> BaseResponse<T> error(String messsage, T data) {
        return new BaseResponse<>("COMMON_ERROR", messsage, data, null);
    }

    public static <T> BaseResponse<T> error(String messsage, T data, String historyContent) {
        return new BaseResponse<>("COMMON_ERROR", messsage, data, historyContent);
    }

    public static <T> BaseResponse<T> error(String code, String messsage) {
        return new BaseResponse<>(code, messsage, null, null);
    }

    public static <T> BaseResponse<T> error(String code, String messsage, String historyContent) {
        return new BaseResponse<>(code, messsage, null, historyContent);
    }

    public static <T> BaseResponse<T> error(String code, String messsage, T data) {
        return new BaseResponse<>(code, messsage, data, null);
    }

    public static <T> BaseResponse<T> error(String code, String messsage, T data, String historyContent) {
        return new BaseResponse<>(code, messsage, data, historyContent);
    }

    public static <T> BaseResponse<T> errorWithType(String message) {
        return new BaseResponse<>("COMMON_ERROR", message, null, null);
    }
}
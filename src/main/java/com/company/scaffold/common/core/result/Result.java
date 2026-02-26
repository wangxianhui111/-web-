package com.company.scaffold.common.core.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.Instant;

@Schema(description = "统一响应结果封装")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Result<T>(
        @Schema(description = "状态码")
        int code,
        @Schema(description = "消息")
        String message,
        @Schema(description = "数据")
        T data,
        @Schema(description = "时间戳")
        long timestamp,
        @Schema(description = "请求ID")
        String requestId
) implements Serializable {

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(200, message, data, Instant.now().toEpochMilli(), null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null, Instant.now().toEpochMilli(), null);
    }

    public static <T> Result<T> fail(String message) {
        return fail(500, message);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return fail(resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return fail(resultCode.getCode(), message);
    }

    public boolean isSuccess() {
        return code == 200;
    }

    public static <T> Result<T> ok() {
        return success();
    }

    public static <T> Result<T> ok(T data) {
        return success(data);
    }
}

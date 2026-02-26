package com.company.scaffold.common.core.result;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "响应状态码枚举")
public enum ResultCode implements ResultCodeInterface {

    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权或登录已过期"),
    FORBIDDEN(403, "没有操作权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    VALIDATION_ERROR(422, "参数校验失败"),

    // 服务端错误 5xx
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    // 业务错误 2xxx
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_DISABLED(2002, "用户已被禁用"),
    USERNAME_PASSWORD_ERROR(2003, "用户名或密码错误"),
    TOKEN_EXPIRED(2004, "Token已过期"),
    TOKEN_INVALID(2005, "Token无效"),
    REPEAT_SUBMIT(2006, "请勿重复提交"),
    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

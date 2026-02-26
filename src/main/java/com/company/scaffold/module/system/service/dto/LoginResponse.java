package com.company.scaffold.module.system.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户登录响应")
public record LoginResponse(
        @Schema(description = "访问令牌")
        String token,

        @Schema(description = "令牌类型")
        String tokenType,

        @Schema(description = "过期时间(秒)")
        Long expiresIn,

        @Schema(description = "用户名")
        String username,

        @Schema(description = "用户ID")
        Long userId
) {
    public static LoginResponse of(String token, Long expiresIn, String username, Long userId) {
        return new LoginResponse(token, "Bearer", expiresIn, username, userId);
    }
}

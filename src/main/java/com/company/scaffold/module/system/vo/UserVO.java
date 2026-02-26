package com.company.scaffold.module.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "用户VO")
public record UserVO(
        @Schema(description = "用户ID")
        Long id,

        @Schema(description = "用户名")
        String username,

        @Schema(description = "昵称")
        String nickname,

        @Schema(description = "邮箱")
        String email,

        @Schema(description = "手机号")
        String phone,

        @Schema(description = "头像URL")
        String avatar,

        @Schema(description = "状态")
        Integer status,

        @Schema(description = "创建时间")
        LocalDateTime createTime
) {}

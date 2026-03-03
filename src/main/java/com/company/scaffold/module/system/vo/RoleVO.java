package com.company.scaffold.module.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "角色VO")
public record RoleVO(
    @Schema(description = "角色ID") Long id,
    @Schema(description = "角色编码") String code,
    @Schema(description = "角色名称") String name,
    @Schema(description = "描述") String description,
    @Schema(description = "状态") Integer status,
    @Schema(description = "创建时间") LocalDateTime createTime
) {}

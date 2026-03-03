package com.company.scaffold.module.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "岗位VO")
public record PostVO(
    @Schema(description = "岗位ID") Long id,
    @Schema(description = "岗位编码") String code,
    @Schema(description = "岗位名称") String name,
    @Schema(description = "描述") String description,
    @Schema(description = "排序") Integer sort,
    @Schema(description = "状态") Integer status,
    @Schema(description = "创建时间") LocalDateTime createTime
) {}

package com.company.scaffold.module.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "部门VO")
public record DeptVO(
    @Schema(description = "部门ID") Long id,
    @Schema(description = "父部门ID") Long parentId,
    @Schema(description = "部门名称") String name,
    @Schema(description = "负责人") String leader,
    @Schema(description = "联系电话") String phone,
    @Schema(description = "邮箱") String email,
    @Schema(description = "排序") Integer sort,
    @Schema(description = "状态") Integer status,
    @Schema(description = "创建时间") LocalDateTime createTime,
    @Schema(description = "子部门") List<DeptVO> children
) {}

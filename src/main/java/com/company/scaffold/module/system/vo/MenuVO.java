package com.company.scaffold.module.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "菜单VO")
public record MenuVO(
    @Schema(description = "菜单ID") Long id,
    @Schema(description = "父菜单ID") Long parentId,
    @Schema(description = "菜单名称") String name,
    @Schema(description = "路由路径") String path,
    @Schema(description = "组件") String component,
    @Schema(description = "图标") String icon,
    @Schema(description = "排序") Integer sort,
    @Schema(description = "显示状态") Integer visible,
    @Schema(description = "权限标识") String permission,
    @Schema(description = "菜单类型") Integer menuType,
    @Schema(description = "创建时间") LocalDateTime createTime,
    @Schema(description = "子菜单") List<MenuVO> children
) {}

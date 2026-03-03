package com.company.scaffold.module.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.company.scaffold.common.core.result.PageResult;
import com.company.scaffold.common.core.result.Result;
import com.company.scaffold.module.system.entity.SysMenu;
import com.company.scaffold.module.system.service.ISysMenuService;
import com.company.scaffold.module.system.vo.MenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
@Validated
@Tag(name = "菜单管理", description = "菜单CRUD接口")
public class SysMenuController {

    private final ISysMenuService menuService;

    @GetMapping("/page")
    @Operation(summary = "分页查询菜单")
    @PreAuthorize("hasAuthority('menu:list')")
    public Result<PageResult<MenuVO>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String name
    ) {
        IPage<MenuVO> page = menuService.pageMenu(current, size, name);
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取菜单详情")
    @PreAuthorize("hasAuthority('menu:query')")
    public Result<MenuVO> getById(@PathVariable Long id) {
        return Result.success(menuService.getMenuById(id));
    }

    @PostMapping
    @Operation(summary = "创建菜单")
    @PreAuthorize("hasAuthority('menu:create')")
    public Result<Void> create(@Valid @RequestBody SysMenu menu) {
        menuService.createMenu(menu);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新菜单")
    @PreAuthorize("hasAuthority('menu:update')")
    public Result<Void> update(@Valid @RequestBody SysMenu menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('menu:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success();
    }
}

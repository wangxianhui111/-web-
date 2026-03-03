package com.company.scaffold.module.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.company.scaffold.common.core.result.PageResult;
import com.company.scaffold.common.core.result.Result;
import com.company.scaffold.module.system.entity.SysRole;
import com.company.scaffold.module.system.service.ISysRoleService;
import com.company.scaffold.module.system.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
@Validated
@Tag(name = "角色管理", description = "角色CRUD接口")
public class SysRoleController {

    private final ISysRoleService roleService;

    @GetMapping("/page")
    @Operation(summary = "分页查询角色")
    @PreAuthorize("hasAuthority('role:list')")
    public Result<PageResult<RoleVO>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String name
    ) {
        IPage<RoleVO> page = roleService.pageRole(current, size, name);
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情")
    @PreAuthorize("hasAuthority('role:query')")
    public Result<RoleVO> getById(@PathVariable Long id) {
        return Result.success(roleService.getRoleById(id));
    }

    @PostMapping
    @Operation(summary = "创建角色")
    @PreAuthorize("hasAuthority('role:create')")
    public Result<Void> create(@Valid @RequestBody SysRole role) {
        roleService.createRole(role);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新角色")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<Void> update(@Valid @RequestBody SysRole role) {
        roleService.updateRole(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('role:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }
}

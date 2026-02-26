package com.company.scaffold.module.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.company.scaffold.common.core.result.PageResult;
import com.company.scaffold.common.core.result.Result;
import com.company.scaffold.module.system.entity.SysUser;
import com.company.scaffold.module.system.service.ISysUserService;
import com.company.scaffold.module.system.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "系统用户CRUD接口")
public class SysUserController {

    private final ISysUserService userService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "支持按用户名、状态筛选")
    @PreAuthorize("hasAuthority('user:list')")
    public Result<PageResult<UserVO>> page(
            @Parameter(description = "当前页码，默认1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小，默认10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status
    ) {
        IPage<UserVO> page = userService.pageUser(current, size, username, status);
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    @PreAuthorize("hasAuthority('user:query')")
    public Result<UserVO> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PostMapping
    @Operation(summary = "创建用户")
    @PreAuthorize("hasAuthority('user:create')")
    public Result<Void> create(@Valid @RequestBody SysUser user) {
        userService.createUser(user);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新用户")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> update(@Valid @RequestBody SysUser user) {
        userService.updateUser(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}

package com.company.scaffold.module.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.company.scaffold.common.core.result.PageResult;
import com.company.scaffold.common.core.result.Result;
import com.company.scaffold.module.system.entity.SysDept;
import com.company.scaffold.module.system.service.ISysDeptService;
import com.company.scaffold.module.system.vo.DeptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
@Validated
@Tag(name = "部门管理", description = "部门CRUD接口")
public class SysDeptController {

    private final ISysDeptService deptService;

    @GetMapping("/page")
    @Operation(summary = "分页查询部门")
    @PreAuthorize("hasAuthority('dept:list')")
    public Result<PageResult<DeptVO>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String name
    ) {
        IPage<DeptVO> page = deptService.pageDept(current, size, name);
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情")
    @PreAuthorize("hasAuthority('dept:query')")
    public Result<DeptVO> getById(@PathVariable Long id) {
        return Result.success(deptService.getDeptById(id));
    }

    @PostMapping
    @Operation(summary = "创建部门")
    @PreAuthorize("hasAuthority('dept:create')")
    public Result<Void> create(@Valid @RequestBody SysDept dept) {
        deptService.createDept(dept);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新部门")
    @PreAuthorize("hasAuthority('dept:update')")
    public Result<Void> update(@Valid @RequestBody SysDept dept) {
        deptService.updateDept(dept);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门")
    @PreAuthorize("hasAuthority('dept:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success();
    }
}

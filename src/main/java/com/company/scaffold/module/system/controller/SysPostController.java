package com.company.scaffold.module.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.company.scaffold.common.core.result.PageResult;
import com.company.scaffold.common.core.result.Result;
import com.company.scaffold.module.system.entity.SysPost;
import com.company.scaffold.module.system.service.ISysPostService;
import com.company.scaffold.module.system.vo.PostVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/post")
@RequiredArgsConstructor
@Validated
@Tag(name = "岗位管理", description = "岗位CRUD接口")
public class SysPostController {

    private final ISysPostService postService;

    @GetMapping("/page")
    @Operation(summary = "分页查询岗位")
    @PreAuthorize("hasAuthority('post:list')")
    public Result<PageResult<PostVO>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String name
    ) {
        IPage<PostVO> page = postService.pagePost(current, size, name);
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取岗位详情")
    @PreAuthorize("hasAuthority('post:query')")
    public Result<PostVO> getById(@PathVariable Long id) {
        return Result.success(postService.getPostById(id));
    }

    @PostMapping
    @Operation(summary = "创建岗位")
    @PreAuthorize("hasAuthority('post:create')")
    public Result<Void> create(@Valid @RequestBody SysPost post) {
        postService.createPost(post);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新岗位")
    @PreAuthorize("hasAuthority('post:update')")
    public Result<Void> update(@Valid @RequestBody SysPost post) {
        postService.updatePost(post);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除岗位")
    @PreAuthorize("hasAuthority('post:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return Result.success();
    }
}

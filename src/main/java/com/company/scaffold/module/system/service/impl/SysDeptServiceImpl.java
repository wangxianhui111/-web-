package com.company.scaffold.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.scaffold.common.core.exception.BusinessException;
import com.company.scaffold.module.system.entity.SysDept;
import com.company.scaffold.module.system.mapper.SysDeptMapper;
import com.company.scaffold.module.system.service.ISysDeptService;
import com.company.scaffold.module.system.vo.DeptVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public IPage<DeptVO> pageDept(Long current, Long size, String name) {
        Page<SysDept> page = new Page<>(current, size);
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, SysDept::getName, name).orderByAsc(SysDept::getSort);
        IPage<SysDept> deptPage = page(page, wrapper);
        return deptPage.convert(this::toVO);
    }

    @Override
    public DeptVO getDeptById(Long id) {
        SysDept dept = getById(id);
        if (dept == null) throw new BusinessException("部门不存在");
        return toVO(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDept(SysDept dept) {
        if (dept.getParentId() == null) dept.setParentId(0L);
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        return save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        SysDept existDept = getById(dept.getId());
        if (existDept == null) throw new BusinessException("部门不存在");
        dept.setUpdateTime(LocalDateTime.now());
        return updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDept(Long id) {
        long childCount = count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        if (childCount > 0) throw new BusinessException("该部门下存在子部门，无法删除");
        return removeById(id);
    }

    private DeptVO toVO(SysDept dept) {
        return new DeptVO(dept.getId(), dept.getParentId(), dept.getName(), dept.getLeader(), dept.getPhone(), dept.getEmail(), dept.getSort(), dept.getStatus(), dept.getCreateTime(), null);
    }
}

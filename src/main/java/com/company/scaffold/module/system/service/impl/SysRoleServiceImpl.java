package com.company.scaffold.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.scaffold.common.core.exception.BusinessException;
import com.company.scaffold.module.system.entity.SysRole;
import com.company.scaffold.module.system.mapper.SysRoleMapper;
import com.company.scaffold.module.system.service.ISysRoleService;
import com.company.scaffold.module.system.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public IPage<RoleVO> pageRole(Long current, Long size, String name) {
        Page<SysRole> page = new Page<>(current, size);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, SysRole::getName, name)
                .orderByDesc(SysRole::getCreateTime);
        IPage<SysRole> rolePage = page(page, wrapper);
        return rolePage.convert(this::toVO);
    }

    @Override
    public RoleVO getRoleById(Long id) {
        SysRole role = getById(id);
        if (role == null) throw new BusinessException("角色不存在");
        return toVO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(SysRole role) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getCode, role.getCode());
        if (getOne(wrapper) != null) throw new BusinessException("角色编码已存在");
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        return save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SysRole role) {
        SysRole existRole = getById(role.getId());
        if (existRole == null) throw new BusinessException("角色不存在");
        role.setUpdateTime(LocalDateTime.now());
        return updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id) {
        SysRole role = getById(id);
        if (role == null) throw new BusinessException("角色不存在");
        return removeById(id);
    }

    private RoleVO toVO(SysRole role) {
        return new RoleVO(role.getId(), role.getCode(), role.getName(), role.getDescription(), role.getStatus(), role.getCreateTime());
    }
}

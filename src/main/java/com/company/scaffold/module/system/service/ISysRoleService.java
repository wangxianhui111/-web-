package com.company.scaffold.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.scaffold.module.system.entity.SysRole;
import com.company.scaffold.module.system.vo.RoleVO;

public interface ISysRoleService extends IService<SysRole> {
    IPage<RoleVO> pageRole(Long current, Long size, String name);
    RoleVO getRoleById(Long id);
    boolean createRole(SysRole role);
    boolean updateRole(SysRole role);
    boolean deleteRole(Long id);
}

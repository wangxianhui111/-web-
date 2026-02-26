package com.company.scaffold.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.scaffold.module.system.entity.SysUser;
import com.company.scaffold.module.system.service.dto.LoginRequest;
import com.company.scaffold.module.system.service.dto.LoginResponse;
import com.company.scaffold.module.system.vo.UserVO;

public interface ISysUserService extends IService<SysUser> {

    SysUser getUserByUsername(String username);

    LoginResponse login(LoginRequest request);

    IPage<UserVO> pageUser(Long current, Long size, String username, Integer status);

    UserVO getUserById(Long id);

    boolean createUser(SysUser user);

    boolean updateUser(SysUser user);

    boolean deleteUser(Long id);
}

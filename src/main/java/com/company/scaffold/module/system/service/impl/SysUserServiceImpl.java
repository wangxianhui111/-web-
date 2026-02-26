package com.company.scaffold.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.scaffold.common.constant.DefaultPassword;
import com.company.scaffold.common.constant.UserStatus;
import com.company.scaffold.common.core.exception.BusinessException;
import com.company.scaffold.common.core.result.ResultCode;
import com.company.scaffold.module.system.entity.SysUser;
import com.company.scaffold.module.system.mapper.SysUserMapper;
import com.company.scaffold.module.system.service.ISysUserService;
import com.company.scaffold.module.system.service.dto.LoginRequest;
import com.company.scaffold.module.system.service.dto.LoginResponse;
import com.company.scaffold.module.system.vo.UserVO;
import com.company.scaffold.security.component.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public SysUser getUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = getUserByUsername(request.username());
        if (user == null) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR);
        }

        if (user.getStatus() != UserStatus.ENABLED) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
        log.info("User logged in, userId: {}", user.getId());

        return LoginResponse.of(token, jwtTokenProvider.getExpirationTime() / 1000, user.getUsername(), user.getId());
    }

    @Override
    public IPage<UserVO> pageUser(Long current, Long size, String username, Integer status) {
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(username != null, SysUser::getUsername, username)
                .eq(status != null, SysUser::getStatus, status)
                .orderByDesc(SysUser::getCreateTime);

        IPage<SysUser> userPage = page(page, wrapper);
        return userPage.convert(this::toVO);
    }

    @Override
    public UserVO getUserById(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return toVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(SysUser user) {
        if (getUserByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(DefaultPassword.DEFAULT));
        user.setStatus(UserStatus.ENABLED);
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        SysUser existUser = getById(user.getId());
        if (existUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return removeById(id);
    }

    private UserVO toVO(SysUser user) {
        return new UserVO(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatar(),
                user.getStatus(),
                user.getCreateTime()
        );
    }
}

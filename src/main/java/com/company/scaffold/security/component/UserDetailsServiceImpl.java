package com.company.scaffold.security.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.scaffold.module.system.entity.SysMenu;
import com.company.scaffold.module.system.entity.SysRole;
import com.company.scaffold.module.system.entity.SysUser;
import com.company.scaffold.module.system.mapper.SysMenuMapper;
import com.company.scaffold.module.system.mapper.SysRoleMapper;
import com.company.scaffold.module.system.mapper.SysUserMapper;
import com.company.scaffold.security.user.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_CACHE_KEY = "scaffold:user:";
    private static final long CACHE_EXPIRE_MINUTES = 30;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String cacheKey = USER_CACHE_KEY + username;
        
        try {
            SecurityUser cachedUser = (SecurityUser) redisTemplate.opsForValue().get(cacheKey);
            if (cachedUser != null) {
                log.debug("从缓存获取用户信息: {}", username);
                return cachedUser;
            }
        } catch (Exception e) {
            log.warn("读取用户缓存失败: {}", e.getMessage());
        }

        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
        );

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        List<SysRole> roles = roleMapper.selectRolesByUserId(user.getId());
        List<SysMenu> menus = menuMapper.selectMenusByUserId(user.getId());

        List<String> roleCodes = roles.stream()
                .map(SysRole::getCode)
                .collect(Collectors.toList());

        List<String> permissions = menus.stream()
                .filter(menu -> menu.getPermission() != null && !menu.getPermission().isEmpty())
                .map(SysMenu::getPermission)
                .collect(Collectors.toList());

        SecurityUser securityUser = SecurityUser.create(user, roleCodes, permissions);
        
        try {
            redisTemplate.opsForValue().set(cacheKey, securityUser, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.debug("用户信息已缓存: {}", username);
        } catch (Exception e) {
            log.warn("缓存用户信息失败: {}", e.getMessage());
        }

        return securityUser;
    }

    public void clearUserCache(String username) {
        try {
            String cacheKey = USER_CACHE_KEY + username;
            redisTemplate.delete(cacheKey);
            log.debug("用户缓存已清除: {}", username);
        } catch (Exception e) {
            log.warn("清除用户缓存失败: {}", e.getMessage());
        }
    }
}

package com.company.scaffold.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.company.scaffold.common.core.result.Result;
import com.company.scaffold.module.system.entity.SysUser;
import com.company.scaffold.module.system.mapper.SysUserMapper;
import com.company.scaffold.security.component.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dev")
public class DevController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/encode")
    public Result<String> encode(@RequestParam String password) {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        String encoded = passwordEncoder.encode(password);
        return Result.success(encoded);
    }
    
    @GetMapping("/verify")
    public Result<Boolean> verify(@RequestParam String password, @RequestParam String hash) {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        boolean matches = passwordEncoder.matches(password, hash);
        return Result.success(matches);
    }
    
    @GetMapping("/hashes")
    public Result<Map<String, String>> getHashes() {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        return Result.success(Map.of(
            "admin123", passwordEncoder.encode("admin123"),
            "123456", passwordEncoder.encode("123456")
        ));
    }

    @GetMapping("/reset-password")
    public Result<String> resetPassword() {
        String username = "admin";
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        String encodedPassword = passwordEncoder.encode("admin123");
        
        SysUser user = new SysUser();
        user.setPassword(encodedPassword);
        
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        userMapper.update(user, wrapper);
        
        userDetailsService.clearUserCache(username);
        
        return Result.success("密码已重置为 admin123");
    }

    @GetMapping("/clear-cache")
    public Result<String> clearCache(@RequestParam(defaultValue = "admin") String username) {
        userDetailsService.clearUserCache(username);
        return Result.success("缓存已清除");
    }
}

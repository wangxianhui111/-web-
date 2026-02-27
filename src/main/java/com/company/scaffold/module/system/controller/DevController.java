package com.company.scaffold.module.system.controller;

import com.company.scaffold.common.core.result.Result;
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
}

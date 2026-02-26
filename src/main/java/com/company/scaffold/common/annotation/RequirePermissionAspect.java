package com.company.scaffold.common.annotation;

import com.company.scaffold.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@Aspect
@Component
public class RequirePermissionAspect {

    @Around("@annotation(com.company.scaffold.common.annotation.RequirePermission)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("未登录或登录已过期");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof SecurityUser securityUser)) {
            throw new SecurityException("用户信息无效");
        }

        if (annotation.permissions().length > 0) {
            List<String> userPermissions = securityUser.getPermissions();
            boolean hasPermission = false;
            for (String permission : annotation.permissions()) {
                if (userPermissions.contains(permission)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                throw new SecurityException("没有权限: " + String.join(",", annotation.permissions()));
            }
        }

        if (annotation.roles().length > 0) {
            List<String> userRoles = securityUser.getRoles();
            boolean hasRole = false;
            for (String role : annotation.roles()) {
                if (userRoles.contains(role)) {
                    hasRole = true;
                    break;
                }
            }
            if (!hasRole) {
                throw new SecurityException("没有角色: " + String.join(",", annotation.roles()));
            }
        }

        return joinPoint.proceed();
    }
}

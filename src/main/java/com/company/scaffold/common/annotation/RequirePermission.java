package com.company.scaffold.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    String value() default "";

    String[] roles() default {};

    String[] permissions() default "";
}

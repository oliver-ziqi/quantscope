package com.developer.quantscope.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RBAC permission requirement.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    /**
     * Required permission code.
     */
    String value();

    /**
     * Whether tenant isolation check is required.
     */
    boolean requireTenant() default true;
}

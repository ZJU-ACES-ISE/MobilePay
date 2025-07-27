package org.software.code.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AdminRole 用于标记方法所需要的管理员角色
 * 简化的RBAC模型，只区分ADMIN和SUPER_ADMIN
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminRole {
    
    /**
     * 需要的最低角色
     * ADMIN: 普通管理员及以上可访问
     * SUPER_ADMIN: 仅超级管理员可访问
     */
    String value() default "ADMIN";
    
    /**
     * 失败提示信息
     */
    String message() default "权限不足";
}

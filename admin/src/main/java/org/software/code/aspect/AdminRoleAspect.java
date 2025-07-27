package org.software.code.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.annotation.AdminRole;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * AdminRoleAspect 是简化的管理员权限控制切面
 * 只区分ADMIN和SUPER_ADMIN两种角色
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Aspect
@Component
@Order(1)
public class AdminRoleAspect {

    private static final Logger logger = LoggerFactory.getLogger(AdminRoleAspect.class);

    @Around("@annotation(org.software.code.common.annotation.AdminRole)")
    public Object checkAdminRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AdminRole adminRole = method.getAnnotation(AdminRole.class);
        
        if (adminRole != null) {
            String currentRole = getAdminRoleFromRequest();
            if (currentRole == null) {
                logger.error("无法获取管理员角色信息");
                throw new BusinessException(ExceptionEnum.ADMIN_PERMISSION_DENIED);
            }
            
            String requiredRole = adminRole.value();
            
            // 检查角色权限
            if (!hasPermission(currentRole, requiredRole)) {
                logger.error("管理员权限不足：需要{}，实际{}", requiredRole, currentRole);
                throw new BusinessException(ExceptionEnum.ADMIN_PERMISSION_DENIED.getCode(), adminRole.message());
            }
        }
        
        return joinPoint.proceed();
    }

    /**
     * 从请求头中获取管理员角色
     */
    private String getAdminRoleFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        
        HttpServletRequest request = attributes.getRequest();
        String role = request.getHeader("X-User-Role");
        return role != null ? role.toUpperCase() : null;
    }

    /**
     * 检查是否有权限
     * 
     * @param currentRole 当前用户角色
     * @param requiredRole 需要的角色
     * @return 是否有权限
     */
    private boolean hasPermission(String currentRole, String requiredRole) {
        if ("SUPER_ADMIN".equals(currentRole)) {
            return true;
        }
        
        if ("SUPER_ADMIN".equals(requiredRole)) {
            return false;
        }
        
        return "ADMIN".equals(currentRole);
    }
}

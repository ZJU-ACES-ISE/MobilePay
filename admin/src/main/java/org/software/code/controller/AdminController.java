package org.software.code.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.annotation.AdminRole;
import org.software.code.common.except.BusinessException;
import org.software.code.common.result.Result;
import org.software.code.dto.AdminLoginDto;
import org.software.code.dto.RefreshTokenDto;
import org.software.code.service.AdminService;
import org.software.code.vo.AdminLoginVo;
import org.software.code.vo.AdminProfileVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * AdminController 是管理员认证控制器，提供管理员登录、登出、个人信息管理等核心认证功能的REST API接口。
 * 
 * <p>本控制器负责处理管理员的身份认证流程，包括：</p>
 * <ul>
 *   <li>管理员登录认证和JWT token生成</li>
 *   <li>管理员登出和token清理</li>
 *   <li>JWT token刷新机制</li>
 *   <li>管理员个人信息查询</li>
 * </ul>
 * 
 * <p>安全特性：</p>
 * <ul>
 *   <li>支持登录失败次数限制，防止暴力破解</li>
 *   <li>记录客户端IP地址，便于安全审计</li>
 *   <li>使用BCrypt加密存储密码</li>
 *   <li>JWT token过期时间管理</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "管理员管理", description = "管理员登录、信息管理等接口")
@Validated
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录认证接口
     * 
     * <p>此接口用于管理员身份认证，验证用户名和密码的正确性，成功后生成JWT访问令牌和刷新令牌。</p>
     * 
     * <p>安全机制：</p>
     * <ul>
     *   <li>密码使用BCrypt加密验证</li>
     *   <li>登录失败次数限制（默认5次），超过限制将临时锁定账户</li>
     *   <li>记录客户端IP地址用于安全审计</li>
     *   <li>账户状态验证（仅激活状态的管理员可登录）</li>
     * </ul>
     * 
     * <p>成功登录后将返回：</p>
     * <ul>
     *   <li>Access Token：用于API访问认证，有效期较短</li>
     *   <li>管理员基本信息：用户名、角色、权限等</li>
     * </ul>
     *
     * @param loginDto 登录请求参数，包含用户名、密码等信息
     * @param request HTTP请求对象，用于获取客户端IP地址
     * @return 登录成功响应，包含JWT tokens和管理员信息
     * @throws BusinessException 当用户名不存在、密码错误、账户被锁定或登录失败次数过多时抛出
     * @see AdminLoginDto 登录请求参数说明
     * @see AdminLoginVo 登录响应数据说明
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登录", description = "管理员使用用户名和密码登录系统，返回JWT token")
    public Result<?> login(@Valid @RequestBody AdminLoginDto loginDto,
                                     HttpServletRequest request) {
        // 获取客户端IP
        String clientIp = getClientIpAddress(request);
        
        AdminLoginVo loginVo = adminService.login(loginDto, clientIp);
        
        logger.info("Admin login successful: {}, ip: {}", loginDto.getUsername(), clientIp);
        return Result.success(loginVo);
    }

    /**
     * 获取当前登录管理员的个人信息
     * 
     * <p>此接口用于获取当前已认证管理员的详细个人信息，包括基本资料、角色权限、登录历史等。</p>
     * 
     * <p>通过API网关传递的X-User-Id请求头来识别当前用户身份，确保数据安全性。</p>
     * 
     * <p>返回信息包括：</p>
     * <ul>
     *   <li>基本信息：用户名、真实姓名、邮箱、手机号</li>
     *   <li>角色信息：角色代码、角色名称、权限列表</li>
     *   <li>状态信息：账户状态、最后登录时间、最后登录IP</li>
     * </ul>
     *
     * @param adminId 管理员ID，通过请求头X-User-Id传递，由API网关解析JWT后注入
     * @return 管理员个人信息详情
     * @throws BusinessException 当管理员不存在时抛出
     * @see AdminProfileVo 个人信息响应数据说明
     */
    @GetMapping("/profile")
    @Operation(summary = "获取管理员信息", description = "获取当前登录管理员的详细信息")
    @AdminRole()
    public Result<?> getProfile(@RequestHeader("X-User-Id") String adminId) {
        AdminProfileVo profileVo = adminService.getProfileById(Long.parseLong(adminId));
        return Result.success(profileVo);
    }

    /**
     * 管理员登出接口
     * 
     * <p>此接口用于管理员主动登出系统，清理相关认证信息和缓存数据。</p>
     * 
     * <p>登出操作包括：</p>
     * <ul>
     *   <li>清除Redis中存储的JWT token缓存</li>
     *   <li>清除Refresh token缓存</li>
     *   <li>记录登出操作日志，包含时间和IP地址</li>
     * </ul>
     *
     * <p>注意：登出后客户端需要清除本地存储的token信息，并重定向到登录页面。</p>
     *
     * @param adminId 管理员ID，通过请求头X-User-Id传递
     * @param request HTTP请求对象，用于获取客户端IP地址
     * @return 操作结果响应
     */
    @PostMapping("/logout")
    @Operation(summary = "管理员退出", description = "管理员退出登录，清除token")
    @AdminRole()
    public Result<?> logout(@RequestHeader("X-User-Id") String adminId,
                           @RequestHeader("Authorization") String token,
                           HttpServletRequest request) {
        String clientIp = getClientIpAddress(request);
        
        adminService.logout(Long.parseLong(adminId), token, clientIp);
        
        logger.info("Admin logout successful: {}, ip: {}", adminId, clientIp);
        return Result.success();
    }

    /**
     * JWT令牌刷新接口
     * 
     * <p>此接口用于在Access Token即将过期时，使用Refresh Token生成新的Access Token</p>
     * 
     * <p>刷新机制：</p>
     * <ul>
     *   <li>验证Refresh Token的有效性和格式</li>
     *   <li>验证当前管理员账户状态（必须为激活状态）</li>
     *   <li>生成新的Access Token和Refresh Token</li>
     *   <li>轮换Refresh Token以增强安全性</li>
     *   <li>更新最后登录时间和IP地址</li>
     *   <li>返回新的token信息和管理员基本信息</li>
     * </ul>
     * 
     * <p>安全注意：刷新token的有效期通常比access token更长，但也有过期时间限制。</p>
     *
     * @param refreshTokenDto 刷新Token请求参数，包含refreshToken
     * @param httpRequest HTTP请求对象，用于获取客户端IP地址
     * @return 新的JWT tokens和管理员信息
     * @throws BusinessException 当Refresh Token无效、过期或管理员账户异常时抛出
     * @see RefreshTokenDto 刷新请求参数说明
     * @see AdminLoginVo 刷新响应数据说明
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用Refresh Token获取新的Access Token")
    public Result<?> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto,
                                  HttpServletRequest httpRequest) {
        String clientIp = getClientIpAddress(httpRequest);
        
        AdminLoginVo loginVo = adminService.refreshToken(refreshTokenDto.getRefreshToken(), clientIp);
        
        logger.info("Admin token refresh successful, ip: {}", clientIp);
        return Result.success(loginVo);
    }

    /**
     * 获取客户端真实IP地址
     * 
     * <p>此方法用于从HTTP请求中提取客户端的真实IP地址，考虑了各种代理和负载均衡的情况。</p>
     * 
     * <p>IP地址获取优先级：</p>
     * <ol>
     *   <li>X-Forwarded-For：标准的代理IP传递头，取第一个IP</li>
     *   <li>X-Real-IP：Nginx等反向代理使用的真实IP头</li>
     *   <li>Proxy-Client-IP：Apache等代理服务器使用</li>
     *   <li>WL-Proxy-Client-IP：WebLogic代理服务器使用</li>
     *   <li>request.getRemoteAddr()：直接连接时的客户端IP</li>
     * </ol>
     * 
     * <p>此方法主要用于安全审计、登录日志记录等场景。</p>
     *
     * @param request HTTP请求对象
     * @return 客户端真实IP地址字符串
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotBlank(xForwardedFor) && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (StrUtil.isNotBlank(xRealIp) && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        String proxyClientIp = request.getHeader("Proxy-Client-IP");
        if (StrUtil.isNotBlank(proxyClientIp) && !"unknown".equalsIgnoreCase(proxyClientIp)) {
            return proxyClientIp;
        }
        
        String wlProxyClientIp = request.getHeader("WL-Proxy-Client-IP");
        if (StrUtil.isNotBlank(wlProxyClientIp) && !"unknown".equalsIgnoreCase(wlProxyClientIp)) {
            return wlProxyClientIp;
        }
        
        return request.getRemoteAddr();
    }
}
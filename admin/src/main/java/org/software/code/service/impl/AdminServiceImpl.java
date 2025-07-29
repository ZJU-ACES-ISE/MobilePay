package org.software.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.consts.AdminConstants;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.util.JwtUtil;
import org.software.code.common.util.RedisUtil;
import org.software.code.dto.AdminLoginDto;
import org.software.code.entity.Admin;
import org.software.code.mapper.AdminMapper;
import org.software.code.service.AdminService;
import org.software.code.vo.AdminLoginVo;
import org.software.code.vo.AdminProfileVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Resource
    private AdminMapper AdminMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisUtil redisUtil;

    public AdminLoginVo login(AdminLoginDto loginDto, String clientIp) {
        // 1. 验证参数
        if (loginDto == null || StrUtil.isBlank(loginDto.getUsername()) || StrUtil.isBlank(loginDto.getPassword())) {
            logger.error("Login parameters invalid: username or password is empty");
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }

        // 2. 检查登录失败次数
        String failKey = AdminConstants.RedisKey.ADMIN_LOGIN_FAIL_PREFIX + loginDto.getUsername();
        String failCountStr = redisUtil.getValue(failKey);
        if (StrUtil.isNotBlank(failCountStr)) {
            int failCount = Integer.parseInt(failCountStr);
            if (failCount >= AdminConstants.DefaultValue.LOGIN_FAIL_MAX_TIMES) {
                logger.error("Admin login fail too many times: {}, count: {}", loginDto.getUsername(), failCount);
                throw new BusinessException(ExceptionEnum.ADMIN_LOGIN_FAIL_LIMIT);
            }
        }

        // 3. 查询管理员信息
        Admin admin = AdminMapper.selectOne(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, loginDto.getUsername()));
        if (admin == null) {
            logger.error("Admin not found: {}", loginDto.getUsername());
            // 记录登录失败次数
            recordLoginFailure(failKey);
            throw new BusinessException(ExceptionEnum.ADMIN_NOT_FOUND);
        }

        // 4. 检查账户状态
        if (!admin.isActive()) {
            logger.error("Admin account is not active: {}, status: {}", admin.getUsername(), admin.getStatus());
            throw new BusinessException(ExceptionEnum.ADMIN_ACCOUNT_INACTIVE);
        }
        // 5. 验证密码
        if (!new BCryptPasswordEncoder().matches(loginDto.getPassword(), admin.getPassword())) {
            logger.error("Admin password error: {}", loginDto.getUsername());
            // 记录登录失败次数
            recordLoginFailure(failKey);
            throw new BusinessException(ExceptionEnum.ADMIN_PASSWORD_ERROR);
        }

        String accessToken = jwtUtil.generateJWToken(admin.getId(), admin.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(admin.getId(), admin.getRole());

        // 7. 存储Refresh Token到Redis
        String refreshKey = "admin:refresh:" + admin.getId();
        String refreshTokenHash = JwtUtil.hashToken(refreshToken);
        redisUtil.setValue(refreshKey, refreshTokenHash, 
                          jwtUtil.getRefreshTokenExpiration(), java.util.concurrent.TimeUnit.SECONDS);

        // 8. 清除登录失败记录
        redisUtil.deleteValue(failKey);

        // 9. 更新最后登录信息
        updateLastLoginInfo(admin.getId(), LocalDateTime.now(), clientIp);

        // 10. 构建响应结果
        AdminLoginVo.AdminInfo adminInfo = AdminLoginVo.AdminInfo.builder()
                .adminId(admin.getId())
                .username(admin.getUsername())
                .role(admin.getRole())
                .lastLoginIp(admin.getLastLoginIp())
                .build();

        AdminLoginVo loginVo = AdminLoginVo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpiration())
                .refreshExpiresIn(jwtUtil.getRefreshTokenExpiration())
                .adminInfo(adminInfo)
                .build();

        logger.info("Admin login successful: {}, role: {}", loginDto.getUsername(), admin.getRole());
        return loginVo;
    }

    public AdminProfileVo getProfileById(Long adminId) {
        // 查询管理员信息
        Admin admin = AdminMapper.selectById(adminId);
        if (admin == null) {
            logger.error("Admin not found by id: {}", adminId);
            throw new BusinessException(ExceptionEnum.ADMIN_NOT_FOUND);
        }

        return AdminProfileVo.builder()
                .adminId(admin.getId())
                .username(admin.getUsername())
                .role(admin.getRole())
                .status(admin.getStatus())
                .lastLoginIp(admin.getLastLoginIp())
                .permissions(getPermissions(admin.getRole()))
                .build();
    }

    @Override
    public AdminLoginVo refreshToken(String refreshToken, String clientIp) {
        // 1. 验证refreshToken格式和有效性
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            logger.error("Refresh token is null or empty");
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            logger.error("Refresh token validation failed");
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }

        try {
            // 2. 从refreshToken中提取用户信息
            Long userId = jwtUtil.extractUserIdFromRefreshToken(refreshToken);
            String role = jwtUtil.extractRoleFromRefreshToken(refreshToken);

            // 3. 验证Redis中的refreshToken
            String refreshKey = "admin:refresh:" + userId;
            String storedTokenHash = redisUtil.getValue(refreshKey);
            String currentTokenHash = JwtUtil.hashToken(refreshToken);

            if (!currentTokenHash.equals(storedTokenHash)) {
                logger.error("Refresh token not found in Redis or hash mismatch: userId={}", userId);
                throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
            }

            // 4. 查询管理员信息并验证状态
            Admin admin = AdminMapper.selectById(userId);
            if (admin == null) {
                logger.error("Admin not found by userId: {}", userId);
                throw new BusinessException(ExceptionEnum.ADMIN_NOT_FOUND);
            }

            if (!admin.isActive()) {
                logger.error("Admin account is not active: userId={}, status={}", userId, admin.getStatus());
                throw new BusinessException(ExceptionEnum.ADMIN_ACCOUNT_INACTIVE);
            }

            // 5. 生成新的Access Token和Refresh Token
            String newAccessToken = jwtUtil.generateJWToken(userId, admin.getRole());
            String newRefreshToken = jwtUtil.generateRefreshToken(userId, admin.getRole());

            // 6. 更新Redis中的Refresh Token（轮换机制）
            String newRefreshHash = JwtUtil.hashToken(newRefreshToken);
            redisUtil.setValue(refreshKey, newRefreshHash, 
                             jwtUtil.getRefreshTokenExpiration(), java.util.concurrent.TimeUnit.SECONDS);

            // 7. 更新最后登录信息
            updateLastLoginInfo(userId, LocalDateTime.now(), clientIp);

            // 8. 构建响应结果
            AdminLoginVo.AdminInfo adminInfo = AdminLoginVo.AdminInfo.builder()
                    .adminId(admin.getId())
                    .username(admin.getUsername())
                    .role(admin.getRole())
                    .lastLoginIp(clientIp)
                    .build();

            AdminLoginVo loginVo = AdminLoginVo.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getAccessTokenExpiration())
                    .refreshExpiresIn(jwtUtil.getRefreshTokenExpiration())
                    .adminInfo(adminInfo)
                    .build();

            logger.info("Admin token refresh successful: username={}, userId={}, ip={}", 
                       admin.getUsername(), userId, clientIp);
            return loginVo;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Refresh token processing error: {}", e.getMessage(), e);
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }
    }

    @Override
    public void logout(Long adminId, String token, String clientIp) {
        // 1. 验证参数
        if (adminId == null) {
            logger.error("Logout parameters invalid: adminId is null");
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }

        try {
            // 2. 查询管理员信息
            Admin admin = AdminMapper.selectById(adminId);
            if (admin == null) {
                logger.warn("Admin not found during logout: {}", adminId);
                return; // 如果管理员不存在，直接返回，不抛异常
            }

            // 3. 如果提供了token，将其添加到Gateway黑名单
            if (token != null && !token.trim().isEmpty()) {
                try {
                    String tokenHash = JwtUtil.hashToken(token);
                    // 计算token剩余有效时间（24小时 - 已过时间）
                    long ttlSeconds = jwtUtil.getAccessTokenExpiration();
                    redisUtil.addTokenToBlacklist(tokenHash, adminId, ttlSeconds);
                    logger.info("Token added to Gateway blacklist: adminId={}, tokenHash={}", adminId, tokenHash);
                } catch (Exception e) {
                    logger.error("Failed to add token to Gateway blacklist: adminId={}, error={}", adminId, e.getMessage());
                    // 继续执行其他登出逻辑，不因黑名单失败而中断
                }
            }

            // 4. 清除Redis中的token缓存
            String tokenKey = AdminConstants.RedisKey.ADMIN_TOKEN_PREFIX + adminId;
            redisUtil.deleteValue(tokenKey);

            // 5. 清除refresh token缓存
            String refreshTokenKey = "admin:refresh:" + adminId;
            redisUtil.deleteValue(refreshTokenKey);

            // 6. 记录登出日志
            logger.info("Admin logout successful: username={}, adminId={}, ip={}", 
                       admin.getUsername(), adminId, clientIp);

        } catch (Exception e) {
            logger.error("Admin logout error: adminId={}, ip={}, error={}", 
                        adminId, clientIp, e.getMessage(), e);
        }
    }

    /**
     * 记录登录失败次数
     */
    private void recordLoginFailure(String failKey) {
        try {
            String countStr = redisUtil.getValue(failKey);
            int count = StrUtil.isBlank(countStr) ? 1 : Integer.parseInt(countStr) + 1;
            redisUtil.setValue(failKey, String.valueOf(count));
            logger.info("Record login failure: key={}, count={}", failKey, count);
        } catch (Exception e) {
            logger.error("Record login failure error: {}", e.getMessage());
        }
    }

    /**
     * 获取角色名称
     */
    private String getRoleName(String role) {
        if ("SUPER_ADMIN".equals(role)) {
            return "超级管理员";
        } else if ("ADMIN".equals(role)) {
            return "普通管理员";
        }
        return "未知角色";
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(String status) {
        if ("ACTIVE".equals(status)) {
            return "正常";
        } else if ("INACTIVE".equals(status)) {
            return "未激活";
        } else if ("LOCKED".equals(status)) {
            return "已锁定";
        }
        return "未知状态";
    }

    /**
     * 获取权限列表
     */
    private String[] getPermissions(String role) {
        if ("SUPER_ADMIN".equals(role)) {
            return new String[]{
                "admin:create", "admin:update", "admin:delete", "admin:view",
                "user:approve", "user:reject", "user:enable", "user:disable", "user:view",
                "site:create", "site:update", "site:delete", "site:view",
                "device:create", "device:update", "device:delete", "device:view",
                "discount:create", "discount:update", "discount:delete", "discount:view",
                "statistics:view"
            };
        } else if ("ADMIN".equals(role)) {
            return new String[]{
                "user:approve", "user:reject", "user:enable", "user:disable", "user:view",
                "site:update", "site:view",
                "device:update", "device:view",
                "discount:create", "discount:update", "discount:view",
                "statistics:view"
            };
        }
        return new String[]{};
    }

    public void updateLastLoginInfo(Long adminId, LocalDateTime loginTime, String loginIp) {
        this.update(
            Wrappers.<Admin>lambdaUpdate()
                .set(Admin::getLastLoginTime, loginTime)
                .set(Admin::getLastLoginIp, loginIp)
                .eq(Admin::getId, adminId)
        );
    }
}

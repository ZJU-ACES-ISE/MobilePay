package org.software.code.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWTUtil 是一个轻量级工具类，专门用于网关 JWT Token 的验证操作。
 * 仅包含网关认证所需的基本 JWT 解析和验证功能。
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    
    // JWT签名密钥，实际应用中应从配置文件或环境变量获取
    private static final String SECRET_KEY = "mobilepay_gateway_jwt_secret_key_2025";

    // Claims键名常量
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";

    /**
     * 从Token中提取用户ID
     *
     * @param token JWT Token字符串
     * @return 用户ID
     * @throws BusinessException 如果解析失败或Token无效
     */
    public static Long extractID(String token) throws BusinessException {
        try {
            Claims claims = extractClaims(token);
            Object userIdObj = claims.get(CLAIM_USER_ID);
            if (userIdObj != null) {
                return Long.valueOf(userIdObj.toString());
            }
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.error("Failed to extract user ID from token: {}, error: {}", token, e.getMessage());
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }
    }

    /**
     * 从Token中提取用户角色
     *
     * @param token JWT Token字符串
     * @return 用户角色字符串
     * @throws BusinessException 如果解析失败或Token无效
     */
    public static String extractRole(String token) throws BusinessException {
        try {
            Claims claims = extractClaims(token);
            Object roleObj = claims.get(CLAIM_ROLE);
            return roleObj != null ? roleObj.toString() : "user";
        } catch (Exception e) {
            logger.error("Failed to extract role from token: {}, error: {}", token, e.getMessage());
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }
    }


    /**
     * 验证Token是否有效
     *
     * @param token JWT Token字符串
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.warn("Token validation failed: {}, error: {}", token, e.getMessage());
            return false;
        }
    }


    /**
     * 提取Token中的Claims
     *
     * @param token JWT Token字符串
     * @return Claims对象
     */
    private static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
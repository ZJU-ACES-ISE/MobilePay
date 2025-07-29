package org.software.code.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtUtil 是用于生成与网关兼容的JWT Token的工具类
 * 支持Access Token和Refresh Token的生成与验证
 *
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // 与网关保持一致的签名密钥
    private static final String SECRET_KEY = "mobilepay_gateway_jwt_secret_key_2025";
    
    // Refresh Token使用不同的签名密钥增强安全性
    private static final String REFRESH_SECRET_KEY = "mobilepay_refresh_jwt_secret_key_2025";

    // Claims键名常量
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";

    // Token过期时间：2小时
    private static final long ACCESS_TOKEN_EXPIRATION = 2 * 60 * 60 * 1000;

    // Refresh Token过期时间：7天
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    /**
     * 生成管理员访问token
     *
     * @param adminId 管理员ID
     * @param role 角色
     * @return JWT Token
     */
    public String generateJWToken(Long adminId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, adminId);
        claims.put(CLAIM_ROLE, role.toLowerCase());
        claims.put(CLAIM_TOKEN_TYPE, "access");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(adminId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 生成刷新token
     *
     * @param adminId 管理员ID
     * @param role 角色
     * @return Refresh JWT Token
     */
    public String generateRefreshToken(Long adminId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, adminId);
        claims.put(CLAIM_ROLE, role.toLowerCase());
        claims.put(CLAIM_TOKEN_TYPE, "refresh");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(adminId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 验证刷新token是否有效
     *
     * @param refreshToken 刷新token
     * @return 是否有效
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = extractRefreshTokenClaims(refreshToken);
            String tokenType = (String) claims.get(CLAIM_TOKEN_TYPE);
            return "refresh".equals(tokenType) && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.warn("Refresh token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从刷新token中提取用户ID
     *
     * @param refreshToken 刷新token
     * @return 用户ID
     */
    public Long extractUserIdFromRefreshToken(String refreshToken) {
        try {
            Claims claims = extractRefreshTokenClaims(refreshToken);
            Object userIdObj = claims.get(CLAIM_USER_ID);
            if (userIdObj != null) {
                return Long.valueOf(userIdObj.toString());
            }
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.error("Failed to extract user ID from refresh token: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh token");
        }
    }

    /**
     * 从刷新token中提取用户角色
     *
     * @param refreshToken 刷新token
     * @return 用户角色
     */
    public String extractRoleFromRefreshToken(String refreshToken) {
        try {
            Claims claims = extractRefreshTokenClaims(refreshToken);
            Object roleObj = claims.get(CLAIM_ROLE);
            return roleObj != null ? roleObj.toString() : "user";
        } catch (Exception e) {
            logger.error("Failed to extract role from refresh token: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh token");
        }
    }

    /**
     * 获取访问token过期时间（秒）
     */
    public long getAccessTokenExpiration() {
        return ACCESS_TOKEN_EXPIRATION / 1000;
    }

    /**
     * 获取刷新token过期时间（秒）
     */
    public long getRefreshTokenExpiration() {
        return REFRESH_TOKEN_EXPIRATION / 1000;
    }

    /**
     * 对Token进行SHA-256哈希
     *
     * @param token JWT Token字符串
     * @return 哈希值
     */
    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            logger.error("Error hashing token: {}", e.getMessage());
            return String.valueOf(token.hashCode());
        }
    }

    /**
     * 提取刷新token中的Claims
     *
     * @param refreshToken 刷新token
     * @return Claims对象
     */
    private Claims extractRefreshTokenClaims(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(refreshToken)
                .getBody();
    }
}
package org.software.code.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWTUtil 是一个工具类，用于处理 JSON Web Token（JWT）的生成操作。
 * 验证功能由网关统一处理，此类主要负责生成Token。
 */
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    
    // JWT签名密钥，与网关保持一致
    private static final String SECRET_KEY = "mobilepay_gateway_jwt_secret_key_2025";
    
    // Token类型常量
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    
    // Claims键名常量
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";
    
    // Bearer前缀
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 生成访问令牌
     *
     * @param userId 用户ID
     * @param expirationTime 过期时间（毫秒）
     * @return JWT令牌字符串，包含"Bearer "前缀
     */
    public static String generateJWToken(long userId, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_ROLE, "user");
        claims.put(CLAIM_TOKEN_TYPE, ACCESS_TOKEN);
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(Long.toString(userId)) // 兼容旧版本
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        
        return BEARER_PREFIX + token;
    }
    
    /**
     * 生成刷新令牌
     *
     * @param userId 用户ID
     * @param expirationTime 过期时间（毫秒）
     * @return JWT令牌字符串，包含"Bearer "前缀
     */
    public static String generateRefreshToken(long userId, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_ROLE, "user");
        claims.put(CLAIM_TOKEN_TYPE, REFRESH_TOKEN);
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(Long.toString(userId)) // 兼容旧版本
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        
        return BEARER_PREFIX + token;
    }
    /**
     * 从Token中提取用户ID
     *
     * @param token JWT Token字符串
     * @return 用户ID
     * @throws BusinessException 如果解析失败或Token无效
     */
    public static Long extractID(String token) throws BusinessException {
        try {
            // 移除Bearer前缀
            if (token.startsWith(BEARER_PREFIX)) {
                token = token.substring(BEARER_PREFIX.length());
            }
            
            Claims claims = extractClaims(token);
            Object userIdObj = claims.get(CLAIM_USER_ID);
            if (userIdObj != null) {
                return Long.valueOf(userIdObj.toString());
            }
            // 兼容旧版本，从subject中获取
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.error("Failed to extract user ID from token: {}, error: {}", token, e.getMessage());
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }
    }
    
    /**
     * 获取Token中的所有Claims
     *
     * @param token JWT Token字符串
     * @return Claims对象
     * @throws BusinessException 如果解析失败或Token无效
     */
    public static Claims extractAllClaims(String token) throws BusinessException {
        try {
            return extractClaims(token);
        } catch (Exception e) {
            logger.error("Failed to extract claims from token: {}, error: {}", token, e.getMessage());
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }
    }

    /**
     * 提取Token中的Claims
     *
     * @param token JWT Token字符串
     * @return Claims对象
     */
    private static Claims extractClaims(String token) {
        // 移除Bearer前缀
        if (token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }
        
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
} 
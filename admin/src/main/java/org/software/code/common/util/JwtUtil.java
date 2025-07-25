package org.software.code.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * GatewayJwtUtil 是用于生成与网关兼容的JWT Token的工具类
 * 生成的token将被网关验证和解析
 *
 */
@Component
public class JwtUtil {

    // 与网关保持一致的签名密钥
    private static final String SECRET_KEY = "mobilepay_gateway_jwt_secret_key_2025";

    // Claims键名常量
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";

    // Token过期时间：24小时
    private static final long ACCESS_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;

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

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(adminId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }


    /**
     * 获取访问token过期时间（秒）
     */
    public long getAccessTokenExpiration() {
        return ACCESS_TOKEN_EXPIRATION / 1000;
    }


}
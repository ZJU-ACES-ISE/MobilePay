package org.software.code.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;

import java.util.Date;
/**
 * JWTUtil 是一个工具类，用于处理 JSON Web Token（JWT）的生成和解析操作。
 * JWT 是一种用于在网络应用间安全传输信息的开放标准（RFC 7519），
 * 它由三部分组成：头部（Header）、载荷（Payload）和签名（Signature），
 * 可以包含用户的身份信息、权限等，并且可以验证信息的完整性和真实性。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
public class JwtUtil {

    // 创建一个日志记录器，用于记录与 JWT 操作相关的日志信息
    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    // 定义 JWT 的签名密钥，用于对 JWT 进行签名和验证，确保 JWT 的完整性和真实性
    // 在实际应用中，应将此密钥妥善保管，避免泄露
    private static String secretKey = "secret_key";

    /**
     * 生成 JWT Token 的方法。
     * 根据传入的用户 ID 和过期时间，使用 Jwts 工具类生成一个 JWT Token。
     *
     * @param id 用户的唯一标识，通常是用户的 ID，会作为 JWT 的主题（Subject）存储在载荷中
     * @param expirationTime  JWT 的过期时间，单位为毫秒，从当前时间开始计算
     * @return 生成的 JWT Token 字符串
     */
    public static String generateJWToken(long id, long expirationTime) {
        // 使用 Jwts.builder() 方法创建一个 JWT 构建器，用于构建 JWT
        // 设置 JWT 的主题为用户 ID 的字符串形式
        // 设置 JWT 的签发时间为当前时间
        // 设置 JWT 的过期时间为当前时间加上传入的过期时间
        // 使用 HS256 签名算法和预定义的密钥对 JWT 进行签名
        // 调用 compact() 方法将构建好的 JWT 转换为字符串形式

        return Jwts.builder()
                .setSubject(Long.toString(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 从给定的 JWT（JSON Web Token）中提取用户 ID 的方法。
     * 解析传入的 JWT，从中获取存储在主题（Subject）中的用户 ID。
     *
     * @param token 待解析的 JWT Token 字符串
     * @return 从 JWT 中提取的用户 ID
     * @throws BusinessException 如果解析过程中出现异常，如 Token 过期、签名验证失败等，
     * 会抛出一个自定义的业务异常，异常类型为 ExceptionEnum.TOKEN_EXPIRED
     */
    public static long extractID(String token) throws BusinessException {
        try {
            // 使用 Jwts.parser() 方法创建一个 JWT 解析器
            // 设置签名密钥，用于验证 JWT 的签名
            // 调用 parseClaimsJws() 方法解析 JWT，并获取其载荷部分
            // 从载荷中获取主题（Subject），并将其转换为长整型的用户 ID
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            // 如果解析过程中出现异常，记录错误日志，包括 Token 信息和异常消息
            logger.error("Failed to extract ID from token: {}, error: {}", token, e.getMessage());
            // 抛出一个自定义的业务异常，提示 Token 过期或无效
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }
    }
}


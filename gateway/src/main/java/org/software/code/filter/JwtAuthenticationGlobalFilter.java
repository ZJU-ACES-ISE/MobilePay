package org.software.code.filter;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.common.util.JwtUtil;
import org.software.code.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * JWT认证全局过滤器，用于验证所有请求的JWT Token。
 * 该过滤器在Spring Cloud Gateway中对所有请求进行JWT验证和权限检查。
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Component
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationGlobalFilter.class);

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private RedisUtil redisUtil;
    @Value("${app.internal.secret}")
    private String internalSecret;

    private static final String TOKEN_PREFIX = "Bearer ";
    
    // White-list
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
        "/user/auth/login",
        "/user/auth/register", 
        "/admin/auth/login",
        "/admin/auth/refresh",
        "/app/v3/api-docs",
        "/app/swagger-ui",
        "/app/user/register",
        "/app/user/login",
        "/app/verifyCode",
        "/app/user/password/reset",
        "/admin/v3/api-docs",
        "/user/v3/api-docs",
        "/swagger-ui",
        "/v3/api-docs",
        "/webjars"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        logger.info("Processing request: {} {}", request.getMethod(), path);

        if (isExcludedPath(path)) {
            logger.info("Skipping authentication for excluded path: {}", path);
            return chain.filter(exchange);
        }

        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            logger.warn("No token found in request headers for path: {}", path);
            return handleAuthError(exchange, ExceptionEnum.TOKEN_NOT_FIND);
        }

        // Auth
        return validateToken(token)
                .flatMap(isValid -> {
                    if (!isValid) {
                        logger.warn("Token validation failed for path: {}", path);
                        return handleAuthError(exchange, ExceptionEnum.TOKEN_EXPIRED);
                    }

                    // 检查token是否在黑名单中
                    if (redisUtil.isTokenBlacklisted(token)) {
                        logger.warn("Token is blacklisted for path: {}", path);
                        return handleAuthError(exchange, ExceptionEnum.TOKEN_EXPIRED);
                    }

                    return addUserInfoToRequest(exchange, token, chain);
                })
                .onErrorResume(throwable -> {
                    logger.error("JWT authentication error for path: {}, error: {}", path, throwable.getMessage());
                    if (throwable instanceof BusinessException) {
                        return handleAuthError(exchange, ExceptionEnum.TOKEN_EXPIRED);
                    }
                    return handleAuthError(exchange, ExceptionEnum.RUN_EXCEPTION);
                });
    }

    /**
     * 检查路径是否在排除列表中
     */
    private boolean isExcludedPath(String path) {
        return EXCLUDE_PATHS.stream().anyMatch(path::startsWith);
    }

    /**
     * 从请求头中提取Token
     */
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 验证Token有效性（包括黑名单检查）
     */
    private Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            // 先检查JWT基本有效性
            if (!JwtUtil.validateToken(token)) {
                return false;
            }

            // 再检查是否在Redis黑名单中
            String tokenHash = JwtUtil.hashToken(token);
            return !redisUtil.isTokenBlacklisted(tokenHash);
        }).onErrorReturn(false);
    }

    /**
     * 将用户信息添加到请求头并继续请求
     */
    private Mono<Void> addUserInfoToRequest(ServerWebExchange exchange, String token, GatewayFilterChain chain) {
        return Mono.fromCallable(() -> {
            // 提取用户信息
            Long userId = JwtUtil.extractID(token);
            String role = JwtUtil.extractRole(token);
            
            logger.info("User authenticated: userId={}, role={}", userId, role);
            
            // 生成内部认证信息
            String internalToken = generateInternalToken(exchange.getRequest());

            // 将用户信息和内部认证信息添加到请求头
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Role", role)
                    .header("X-Internal-Token", internalToken)
                    .header("X-Gateway-Source", "mobilepay-gateway")
                    .header("Authorization", token)
                    .build();
            
            return exchange.mutate().request(modifiedRequest).build();
        }).flatMap(modifiedExchange -> checkPermissions(modifiedExchange, token)
                .flatMap(hasPermission -> {
                    if (!hasPermission) {
                        return handleAuthError(exchange, ExceptionEnum.GATEWAY_PERMISSION_DENIED);
                    }
                    return chain.filter(modifiedExchange);
                }));
    }

    /**
     * 生成内部认证Token
     *
     * <p>基于请求方法、URI和内部密钥生成HMAC-SHA256签名，
     * 不再依赖时间戳，简化了认证流程。</p>
     */
    private String generateInternalToken(ServerHttpRequest request) {
        // 构建签名内容：method + uri + secret
        String method = request.getMethod() != null ? request.getMethod().name() : "GET";
        String uri = request.getURI().getPath();
        String signContent = method + "|" + uri + "|" + internalSecret;

        // 使用HMAC-SHA256生成签名
        HMac hmac = new HMac(HmacAlgorithm.HmacSHA256, internalSecret.getBytes(StandardCharsets.UTF_8));
        return hmac.digestHex(signContent);
    }

    /**
     * 检查用户权限（基于路径和角色）
     */
    private Mono<Boolean> checkPermissions(ServerWebExchange exchange, String token) {
        return Mono.fromCallable(() -> {
            String path = exchange.getRequest().getURI().getPath();
            String role = JwtUtil.extractRole(token);
            
            // 基于路径的权限检查
            if (path.startsWith("/admin/")) {
                // admin路径需要admin或super_admin权限
                return "admin".equals(role) || "super_admin".equals(role);
            } else if (path.startsWith("/user/")) {
                // user路径允许所有已认证用户
                return true;
            }
            return true;
        }).onErrorReturn(false);
    }

    /**
     * 处理认证错误
     */
    private Mono<Void> handleAuthError(ServerWebExchange exchange, ExceptionEnum exceptionEnum) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Result<?> result = Result.failed(exceptionEnum.getMsg());
        
        try {
            String body = objectMapper.writeValueAsString(result);
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Flux.just(buffer));
        } catch (JsonProcessingException e) {
            logger.error("Error serializing authentication error response", e);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
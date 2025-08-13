package org.software.code.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 内部服务认证过滤器（简化版）
 * 
 * <p>用于验证请求确实来自合法的网关，防止恶意服务绕过网关直接访问admin服务。</p>
 * 
 * <p>简化的认证机制：</p>
 * <ul>
 *   <li>移除了时间戳检查，简化了防重放攻击机制</li>
 *   <li>使用固定密钥和请求信息生成HMAC签名</li>
 *   <li>适用于内网环境，减少了时间同步的复杂性</li>
 * </ul>
 * 
 * <p>认证流程：</p>
 * <ol>
 *   <li>检查网关来源标识头（X-Gateway-Source）</li>
 *   <li>验证内部认证Token（基于请求方法、URI和密钥的HMAC签名）</li>
 *   <li>通过验证后放行请求</li>
 * </ol>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Component
@Order(1)
public class InternalServiceAuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(InternalServiceAuthFilter.class);
    
    /**
     * 内部认证Token请求头名称
     */
    private static final String INTERNAL_TOKEN_HEADER = "X-Internal-Token";
    
    /**
     * 网关来源标识请求头名称
     */
    private static final String GATEWAY_SOURCE_HEADER = "X-Gateway-Source";
    
    /**
     * 预期的网关来源标识值
     */
    private static final String EXPECTED_GATEWAY_SOURCE = "mobilepay-gateway";
    
    /**
     * 内部服务通信密钥，从配置文件读取
     */
    @Value("${app.internal.secret}")
    private String internalSecret;
    
    /**
     * 内部认证开关，可通过配置文件控制，默认启用
     */
    @Value("${app.internal.enabled:true}")
    private boolean authEnabled;
    
    /**
     * JSON序列化工具
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestPath = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        
        // 如果认证未启用，直接放行（开发环境可用）
        if (!authEnabled) {
            logger.debug("内部服务认证已禁用，直接放行");
            chain.doFilter(request, response);
            return;
        }
        
        // CORS预检请求直接放行
        if ("OPTIONS".equals(method)) {
            logger.debug("CORS预检请求，跳过内部认证：{} {}", method, requestPath);
            chain.doFilter(request, response);
            return;
        }
        
        // 跳过白名单路径，无需内部认证
        if (isExcludedPath(requestPath)) {
            logger.debug("跳过内部认证的路径：{}", requestPath);
            chain.doFilter(request, response);
            return;
        }
        
        // 验证内部服务认证
        if (!validateInternalAuth(httpRequest)) {
            logger.warn("内部服务认证失败：{} {}", method, requestPath);
            handleAuthFailure(httpResponse, "内部服务认证失败");
            return;
        }
        
        logger.debug("内部服务认证成功：{} {}", method, requestPath);
        chain.doFilter(request, response);
    }
    
    /**
     * 验证内部服务认证（简化版）
     * 
     * <p>简化的认证流程：</p>
     * <ol>
     *   <li>检查必要的请求头（Token和网关来源）</li>
     *   <li>验证网关来源标识</li>
     *   <li>验证HMAC签名</li>
     * </ol>
     *
     * @param request HTTP请求对象
     * @return 是否通过认证
     */
    private boolean validateInternalAuth(HttpServletRequest request) {
        try {
            // 1. 检查必要的请求头
            String internalToken = request.getHeader(INTERNAL_TOKEN_HEADER);
            String gatewaySource = request.getHeader(GATEWAY_SOURCE_HEADER);
            
            if (StrUtil.isBlank(internalToken)) {
                logger.warn("缺少内部认证Token请求头");
                return false;
            }
            
            // 2. 验证网关来源标识
            if (!EXPECTED_GATEWAY_SOURCE.equals(gatewaySource)) {
                logger.warn("无效的网关来源标识：{}", gatewaySource);
                return false;
            }
            
            // 3. 验证HMAC签名
            String expectedToken = generateInternalToken(request);
            if (!internalToken.equals(expectedToken)) {
                logger.warn("内部Token验证失败，期望：{}，实际：{}", expectedToken, internalToken);
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            logger.error("内部服务认证异常：{}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 生成内部认证Token（简化版）
     * 
     * <p>基于请求方法、URI和内部密钥生成HMAC-SHA256签名，
     * 不再依赖时间戳，简化了认证流程。</p>
     *
     * @param request HTTP请求对象
     * @return HMAC签名字符串
     */
    private String generateInternalToken(HttpServletRequest request) {
        // 构建签名内容：method + uri + secret
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String signContent = method + "|" + uri + "|" + internalSecret;
        
        // 使用HMAC-SHA256生成签名
        HMac hmac = new HMac(HmacAlgorithm.HmacSHA256, internalSecret.getBytes(StandardCharsets.UTF_8));
        return hmac.digestHex(signContent);
    }
    
    /**
     * 检查是否为排除路径（不需要内部认证）
     * 
     * <p>以下路径不需要内部认证：</p>
     * <ul>
     *   <li>登录接口：/auth/login</li>
     *   <li>Swagger文档：/swagger-ui/**</li>
     *   <li>API文档：/v3/api-docs/**</li>
     *   <li>静态资源：/webjars/**</li>
     *   <li>健康检查：/actuator/health</li>
     * </ul>
     *
     * @param path 请求路径
     * @return 是否为排除路径
     */
    private boolean isExcludedPath(String path) {
        // 登录接口和公开接口不需要内部认证
        // 注意：gateway已经使用StripPrefix=1移除了/admin前缀
        return path.equals("/auth/login") || 
               path.startsWith("/swagger-ui") || 
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/webjars") ||
               path.equals("/actuator/health");
    }
    
    /**
     * 处理认证失败的情况
     * 
     * <p>当内部认证失败时，返回403 Forbidden状态码和错误信息。</p>
     *
     * @param response HTTP响应对象
     * @param message 错误消息
     * @throws IOException IO异常
     */
    private void handleAuthFailure(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        Result<?> result = Result.failed(message);
        String jsonResponse = objectMapper.writeValueAsString(result);
        
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
package org.software.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域资源共享（CORS）配置类，用于解决前端跨域请求后端接口的问题。
 * 通过创建一个 CorsFilter Bean，允许所有来源、所有请求头和所有请求方法的跨域请求，
 * 并支持携带凭证信息进行跨域请求。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
public class CorsConfig {
    /**
     * 创建一个 CorsFilter Bean，用于处理跨域请求。
     * 该方法配置了跨域请求的相关规则，包括允许携带凭证、允许所有来源、所有请求头和所有请求方法，
     * 并将这些规则应用到所有的请求路径上。
     *
     * @return 配置好的 CorsFilter 实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建一个基于 URL 的跨域配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 创建一个跨域配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 允许跨域请求携带凭证信息，如 Cookie 等
        config.setAllowCredentials(true);
        // 允许所有域名进行跨域调用
        config.addAllowedOrigin("*");
        // 允许任何请求头
        config.addAllowedHeader("*");
        // 允许任何方法（POST、GET 等）
        config.addAllowedMethod("*");
        // 将跨域配置注册到所有的请求路径上
        source.registerCorsConfiguration("/**", config);
        // 创建并返回 CorsFilter 实例
        return new CorsFilter(source);
    }
}
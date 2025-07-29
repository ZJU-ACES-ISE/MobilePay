package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AdminLoginVo 是管理员登录响应视图对象，用于封装登录成功后返回的信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginVo {

    /**
     * 访问token
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 刷新token
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * token类型
     */
    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    /**
     * 访问token过期时间（秒）
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * 刷新token过期时间（秒）
     */
    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;

    /**
     * 管理员信息
     */
    @JsonProperty("admin_info")
    private AdminInfo adminInfo;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminInfo {
        /**
         * 管理员ID
         */
        @JsonProperty("admin_id")
        private Long adminId;

        /**
         * 用户名
         */
        @JsonProperty("username")
        private String username;
        /**
         * 角色
         */
        @JsonProperty("role")
        private String role;
        /**
         * 最后登录时间
         */
        @JsonProperty("last_login_time")
        private LocalDateTime lastLoginTime;

        /**
         * 最后登录IP
         */
        @JsonProperty("last_login_ip")
        private String lastLoginIp;
    }
}
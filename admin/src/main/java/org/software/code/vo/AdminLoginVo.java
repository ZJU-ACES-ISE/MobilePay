package org.software.code.vo;

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
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * token类型
     */
    private String tokenType = "Bearer";

    /**
     * 访问token过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 刷新token过期时间（秒）
     */
    private Long refreshExpiresIn;

    /**
     * 管理员信息
     */
    private AdminInfo adminInfo;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminInfo {
        /**
         * 管理员ID
         */
        private Long adminId;

        /**
         * 用户名
         */
        private String username;
        /**
         * 角色
         */
        private String role;
        /**
         * 最后登录时间
         */
        private LocalDateTime lastLoginTime;

        /**
         * 最后登录IP
         */
        private String lastLoginIp;
    }
}
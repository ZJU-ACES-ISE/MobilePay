package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AdminProfileVo 是管理员信息视图对象，用于封装管理员详细信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProfileVo {

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
     * 状态
     */
    @JsonProperty("status")
    private String status;

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

    /**
     * 创建时间
     */
    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    /**
     * 权限列表
     */
    @JsonProperty("permissions")
    private String[] permissions;

    /**
     * 获取角色名称
     */
    public String getRoleName() {
        if ("SUPER_ADMIN".equals(this.role)) {
            return "超级管理员";
        } else if ("ADMIN".equals(this.role)) {
            return "普通管理员";
        }
        return "未知角色";
    }

    /**
     * 获取状态名称
     */
    public String getStatusName() {
        if ("ACTIVE".equals(this.status)) {
            return "正常";
        } else if ("INACTIVE".equals(this.status)) {
            return "未激活";
        } else if ("LOCKED".equals(this.status)) {
            return "已锁定";
        }
        return "未知状态";
    }
}
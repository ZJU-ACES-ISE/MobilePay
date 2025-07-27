package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * UserListVo 是用户列表视图对象，用于封装用户列表信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListVo {

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Long userId;

    /**
     * 手机号
     */
    @JsonProperty("phone")
    private String phone;

    /**
     * 用户名
     */
    @JsonProperty("username")
    private String username;

    /**
     * 真实姓名
     */
    @JsonProperty("real_name")
    private String realName;

    /**
     * 邮箱
     */
    @JsonProperty("email")
    private String email;

    /**
     * 头像URL
     */
    @JsonProperty("avatar")
    private String avatar;

    /**
     * 状态
     */
    @JsonProperty("status")
    private String status;

    /**
     * 状态名称
     */
    @JsonProperty("status_name")
    private String statusName;

    /**
     * 余额
     */
    @JsonProperty("balance")
    private BigDecimal balance;

    /**
     * 用户类型
     */
    @JsonProperty("user_type")
    private String userType;

    /**
     * 用户类型名称
     */
    @JsonProperty("user_type_name")
    private String userTypeName;

    /**
     * 创建时间
     */
    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @JsonProperty("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 获取状态名称
     */
    public String getStatusName() {
        if ("PENDING".equals(this.status)) {
            return "待审核";
        } else if ("APPROVED".equals(this.status)) {
            return "已通过";
        } else if ("REJECTED".equals(this.status)) {
            return "已拒绝";
        } else if ("DISABLED".equals(this.status)) {
            return "已禁用";
        }
        return "未知状态";
    }

    /**
     * 获取用户类型名称
     */
    public String getUserTypeName() {
        if ("VIP".equals(this.userType)) {
            return "VIP用户";
        } else if ("NORMAL".equals(this.userType)) {
            return "普通用户";
        }
        return "未知类型";
    }
}
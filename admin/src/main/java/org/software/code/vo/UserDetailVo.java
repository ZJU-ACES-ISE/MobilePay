package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UserDetailVo 是用户详情视图对象，用于封装用户详细信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVo {

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
     * 昵称
     */
    @JsonProperty("nickname")
    private String nickname;

    /**
     * 真实姓名
     */
    @JsonProperty("real_name")
    private String realName;

    /**
     * 身份证号
     */
    @JsonProperty("id_card")
    private String idCard;

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
     * 审核记录列表
     */
    @JsonProperty("audit_records")
    private List<AuditRecordVo> auditRecords;

    /**
     * 审核记录视图对象
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuditRecordVo {
        /**
         * 审核ID
         */
        @JsonProperty("audit_id")
        private Long auditId;

        /**
         * 审核类型
         */
        @JsonProperty("audit_type")
        private String auditType;

        /**
         * 审核类型名称
         */
        @JsonProperty("audit_type_name")
        private String auditTypeName;

        /**
         * 审核结果
         */
        @JsonProperty("audit_result")
        private String auditResult;

        /**
         * 审核结果名称
         */
        @JsonProperty("audit_result_name")
        private String auditResultName;

        /**
         * 审核原因
         */
        @JsonProperty("audit_reason")
        private String auditReason;

        /**
         * 审核管理员
         */
        @JsonProperty("admin_name")
        private String adminName;

        /**
         * 审核时间
         */
        @JsonProperty("created_time")
        private LocalDateTime createdTime;

        /**
         * 获取审核类型名称
         */
        public String getAuditTypeName() {
            if ("REGISTER".equals(this.auditType)) {
                return "注册审核";
            } else if ("PROFILE_UPDATE".equals(this.auditType)) {
                return "资料更新审核";
            }
            return "未知类型";
        }

        /**
         * 获取审核结果名称
         */
        public String getAuditResultName() {
            if ("APPROVED".equals(this.auditResult)) {
                return "通过";
            } else if ("REJECTED".equals(this.auditResult)) {
                return "拒绝";
            }
            return "未知结果";
        }
    }

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
}
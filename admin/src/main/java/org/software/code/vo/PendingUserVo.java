package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 待审核用户视图对象（合并user + user_verification信息）
 *
 * <p>此VO对象用于展示待身份验证审核的用户信息，合并了用户基本信息和身份验证详细信息。</p>
 * 
 * <p>数据来源：</p>
 * <ul>
 *   <li>用户基本信息：来自user表</li>
 *   <li>身份验证信息：来自user_verification表（status='pending'）</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingUserVo {

    // ========== 用户基本信息 (来自user表) ==========
    
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
     * 头像URL
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;

    /**
     * 用户状态
     */
    @JsonProperty("status")
    private String status;

    /**
     * 用户注册时间
     */
    @JsonProperty("create_time")
    private LocalDateTime createTime;

    /**
     * 用户信息更新时间
     */
    @JsonProperty("update_time")
    private LocalDateTime updateTime;

    // ========== 身份验证信息 (来自user_verification表) ==========

    /**
     * 身份验证记录ID
     */
    @JsonProperty("verification_id")
    private Long verificationId;

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
     * 身份证正面照片URL
     */
    @JsonProperty("id_card_front")
    private String idCardFront;

    /**
     * 身份证背面照片URL
     */
    @JsonProperty("id_card_back")
    private String idCardBack;

    /**
     * 身份验证状态
     */
    @JsonProperty("verification_status")
    private String verificationStatus;

    /**
     * 拒绝原因（审核拒绝时填写）
     */
    @JsonProperty("reject_reason")
    private String rejectReason;

    /**
     * 身份验证提交时间
     */
    @JsonProperty("submit_time")
    private LocalDateTime submitTime;

    /**
     * 审核时间
     */
    @JsonProperty("audit_time")
    private LocalDateTime auditTime;

}
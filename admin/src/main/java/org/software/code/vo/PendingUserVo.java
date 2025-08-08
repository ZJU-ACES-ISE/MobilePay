package org.software.code.vo;

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
    private Long userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 用户注册时间
     */
    private LocalDateTime createTime;

    /**
     * 用户信息更新时间
     */
    private LocalDateTime updateTime;

    // ========== 身份验证信息 (来自user_verification表) ==========

    /**
     * 身份验证记录ID
     */
    private Long verificationId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证正面照片URL
     */
    private String idCardFront;

    /**
     * 身份证背面照片URL
     */
    private String idCardBack;

    /**
     * 身份验证状态
     */
    private String verificationStatus;

    /**
     * 拒绝原因（审核拒绝时填写）
     */
    private String rejectReason;

    /**
     * 身份验证提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

}
package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户资料审核实体类
 */
@Data
@TableName("user_verification")
public class UserAudit {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;
    
    /**
     * 身份证正面照片URL
     */
    @TableField("id_card_front")
    private String idCardFront;
    
    /**
     * 身份证背面照片URL
     */
    @TableField("id_card_back")
    private String idCardBack;
    
    /**
     * 审核状态：pending-待审核, approved-已通过, rejected-已拒绝
     */
    @TableField("status")
    private String status;
    
    /**
     * 拒绝原因（仅在状态为rejected时有值）
     */
    @TableField("reject_reason")
    private String rejectReason;
    
    /**
     * 提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;
    
    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;
} 
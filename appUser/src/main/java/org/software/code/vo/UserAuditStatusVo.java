package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户资料审核状态响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户资料审核状态响应")
public class UserAuditStatusVo {
    
    /**
     * 审核状态：pending-待审核, approved-已通过, rejected-已拒绝, none-未提交
     */
    @Schema(description = "审核状态", required = true, example = "pending")
    private String status;
    
    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", required = false, example = "张三")
    private String realName;
    
    /**
     * 拒绝原因（仅在状态为rejected时有值）
     */
    @Schema(description = "拒绝原因", required = false, example = "身份证照片不清晰")
    private String rejectReason;
    
    /**
     * 提交时间
     */
    @Schema(description = "提交时间", required = false, example = "2023-07-01 12:00:00")
    private LocalDateTime submitTime;
    
    /**
     * 审核时间
     */
    @Schema(description = "审核时间", required = false, example = "2023-07-02 14:30:00")
    private LocalDateTime auditTime;
} 
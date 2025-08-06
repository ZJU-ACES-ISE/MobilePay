package org.software.code.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收款码信息实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("receipt_code")
public class ReceiptCode {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 收款码唯一编号
     */
    private String receiptCodeId;
    
    /**
     * 收款用户ID
     */
    private Long userId;
    
    /**
     * 二维码URL
     */
    private String codeUrl;
    
    /**
     * 预设收款金额（可为空）
     */
    private BigDecimal amount;
    
    /**
     * 二维码过期时间
     */
    private LocalDateTime expireAt;
    
    /**
     * 事件时间（二维码创建时间）
     */
    private LocalDateTime timestamp;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

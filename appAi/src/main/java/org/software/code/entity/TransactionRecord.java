package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录实体
 */
@Data
@TableName("transaction_record")
public class TransactionRecord {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 交易金额
     */
    private BigDecimal amount;
    
    /**
     * 交易类型 (PAYMENT, TRANSFER, RECHARGE等)
     */
    private String transactionType;
    
    /**
     * 交易描述
     */
    private String description;
    
    /**
     * 商家名称
     */
    private String merchantName;
    
    /**
     * 交易类别 (FOOD, TRANSPORT, SHOPPING等)
     */
    private String category;
    
    /**
     * 交易状态 (SUCCESS, FAILED, PENDING)
     */
    private String status;
    
    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 
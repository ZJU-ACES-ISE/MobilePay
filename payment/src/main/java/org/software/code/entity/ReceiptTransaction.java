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
 * 收款交易记录实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("receipt_transaction")
public class ReceiptTransaction {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 交易编号
     */
    private String transactionId;
    
    /**
     * 付款用户ID
     */
    private Long payerId;
    
    /**
     * 收款用户ID
     */
    private Long receiverId;
    
    /**
     * 收款人姓名
     */
    private String receiverName;
    
    /**
     * 收款金额
     */
    private BigDecimal amount;
    
    /**
     * 对应收款码ID（可为空）
     */
    private String receiptCodeId;
    
    /**
     * 收款完成时间
     */
    private LocalDateTime timestamp;
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;
}

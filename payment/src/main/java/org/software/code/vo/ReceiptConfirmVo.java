package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收款确认响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptConfirmVo {
    
    /**
     * 交易流水号
     */
    private String transactionId;
    
    /**
     * 支付人ID
     */
    private Long payerId;
    
    /**
     * 收款人ID
     */
    private Long receiverId;
    
    /**
     * 收款人姓名
     */
    private String receiverName;
    
    /**
     * 交易金额
     */
    private String amount;
    
    /**
     * 交易时间
     */
    private String timestamp;
    
    /**
     * 收款码ID
     */
    private String receiptCodeId;
} 
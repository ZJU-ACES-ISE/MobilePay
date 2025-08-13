package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收款记录视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRecordVo {
    
    /**
     * 交易流水id
     */
    private String transactionId;
    
    /**
     * 付款人id
     */
    private Long payerId;
    
    /**
     * 收款人id
     */
    private Long receiverId;
    
    /**
     * 收款人姓名
     */
    private String receiverName;
    
    /**
     * 金额
     */
    private String amount;
    
    /**
     * 交易时间
     */
    private String timestamp;
} 
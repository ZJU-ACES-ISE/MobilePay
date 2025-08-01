package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付确认响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmVo {
    
    /**
     * 交易流水号
     */
    private String transactionId;
    
    /**
     * 支付后账户余额
     */
    private String balanceAfter;
    
    /**
     * 支付金额
     */
    private String amount;
    
    /**
     * 交易类型：1=收入，2=转出
     */
    private Integer type;
    
    /**
     * 支出分类
     */
    private Integer bizCategory;
    
    /**
     * 交易对象ID
     */
    private Long targetId;
    
    /**
     * 交易对象名称
     */
    private String targetName;
    
    /**
     * 交易对象类型：1=用户，2=商户，3=银行卡
     */
    private Integer targetType;
    
    /**
     * 交易完成时间，ISO 8601格式
     */
    private String completeTime;
} 
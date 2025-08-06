package org.software.code.ai.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录实体类
 */
@Data
public class TransactionRecord {
    /**
     * 交易ID
     */
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
     * 交易类型
     */
    private String type;
    
    /**
     * 交易分类
     */
    private String category;
    
    /**
     * 交易描述
     */
    private String description;
    
    /**
     * 交易时间
     */
    private Date transactionTime;
    
    /**
     * 交易状态
     */
    private String status;
    
    /**
     * 交易对方
     */
    private String counterparty;
    
    /**
     * 交易地点
     */
    private String location;
    
    /**
     * 交易渠道
     */
    private String channel;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}

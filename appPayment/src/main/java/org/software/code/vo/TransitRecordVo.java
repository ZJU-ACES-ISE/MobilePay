package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出行记录响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitRecordVo {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 交通方式
     */
    private String mode;
    
    /**
     * 入站站点ID
     */
    private Long entrySiteId;
    
    /**
     * 入站站点名称
     */
    private String entrySiteName;
    
    /**
     * 入站站点线路
     */
    private String entrySiteLine;
    
    /**
     * 出站站点ID
     */
    private Long exitSiteId;
    
    /**
     * 出站站点名称
     */
    private String exitSiteName;
    
    /**
     * 出站站点线路
     */
    private String exitSiteLine;
    
    /**
     * 入站时间
     */
    private LocalDateTime entryTime;
    
    /**
     * 出站时间
     */
    private LocalDateTime exitTime;
    
    /**
     * 原始金额
     */
    private BigDecimal amount;
    
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 实际金额
     */
    private BigDecimal actualAmount;
    
    /**
     * 状态（0:正常, 1:异常）
     */
    private Integer status;
    
    /**
     * 异常原因
     */
    private String reason;
    
    /**
     * 交易ID
     */
    private String transactionId;
} 
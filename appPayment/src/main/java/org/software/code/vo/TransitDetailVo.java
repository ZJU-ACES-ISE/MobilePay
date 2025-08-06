package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出行详情VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitDetailVo {
    
    /**
     * 出行ID
     */
    private String transitId;
    
    /**
     * 出行方式
     */
    private String mode;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 进站站点ID
     */
    private Long entrySiteId;
    
    /**
     * 进站站点名称
     */
    private String entrySiteName;
    
    /**
     * 进站线路
     */
    private String entrySiteLine;
    
    /**
     * 进站时间
     */
    private LocalDateTime entryTime;
    
    /**
     * 出站站点ID
     */
    private Long exitSiteId;
    
    /**
     * 出站站点名称
     */
    private String exitSiteName;
    
    /**
     * 出站线路
     */
    private String exitSiteLine;
    
    /**
     * 出站时间
     */
    private LocalDateTime exitTime;
    
    /**
     * 费用
     */
    private BigDecimal amount;
    
    /**
     * 实际费用
     */
    private BigDecimal actualAmount;
    
    /**
     * 状态（-1=未出站，0=出行正常，1=支付异常，2=出行异常）
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
    
    /**
     * 持续时间（分钟）
     */
    private Long duration;
} 
package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 费用计算响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FareCalculationVo {
    
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
     * 交通方式
     */
    private String transitType;
    
    /**
     * 费用
     */
    private BigDecimal amount;
} 
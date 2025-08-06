package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 二维码解析结果视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeParseResultVo {
    
    /**
     * 目标ID（收款方ID）
     */
    private Long targetId;
    
    /**
     * 目标名称（收款方名称）
     */
    private String targetName;
    
    /**
     * 目标类型（1-个人，2-商户）
     */
    private Integer targetType;
    
    /**
     * 业务类别（1-收款，2-付款，3-转账）
     */
    private Integer bizCategory;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 金额（单位：分）
     */
    private Integer amount;
    
    /**
     * 折扣（单位：分）
     */
    private Integer discount;
    
    /**
     * 实际金额（单位：分）
     */
    private Integer actualAmount;
} 
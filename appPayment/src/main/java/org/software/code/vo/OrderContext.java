package org.software.code.vo;

import lombok.Builder;
import lombok.Data;
import org.software.code.entity.DiscountStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单上下文类
 * 用于传递订单相关信息给折扣策略
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Data
@Builder
public class OrderContext {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 目标ID（商家ID、站点ID等）
     */
    private Long targetId;
    
    /**
     * 目标类型
     */
    private Integer targetType;
    
    /**
     * 业务分类
     */
    private Integer bizCategory;
    
    /**
     * 原始金额
     */
    private BigDecimal originalAmount;
    
    /**
     * 城市编码
     */
    private String cityCode;
    
    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;
    
    /**
     * 折扣策略
     */
    private DiscountStrategy discountStrategy;
    
    /**
     * 获取最大折扣限制
     */
    public BigDecimal getMaxDiscount() {
        return discountStrategy != null ? discountStrategy.getMaxDiscount() : BigDecimal.ZERO;
    }
} 
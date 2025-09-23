package org.software.code.strategy;

import org.software.code.entity.DiscountStrategy;
import org.software.code.vo.OrderContext;

import java.math.BigDecimal;

/**
 * 折扣计算策略接口
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface DiscountCalculationStrategy {
    
    /**
     * 计算折扣金额
     * 
     * @param originalAmount 原始金额
     * @param context 订单上下文
     * @return 折扣金额
     */
    BigDecimal calculate(BigDecimal originalAmount, OrderContext context);
    
    /**
     * 获取策略类型
     * 
     * @return 策略类型
     */
    String getStrategyType();
} 
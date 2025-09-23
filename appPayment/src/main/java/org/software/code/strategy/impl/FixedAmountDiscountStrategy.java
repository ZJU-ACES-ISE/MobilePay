package org.software.code.strategy.impl;

import org.software.code.strategy.DiscountCalculationStrategy;
import org.software.code.vo.OrderContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 固定金额折扣策略实现类
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Component
public class FixedAmountDiscountStrategy implements DiscountCalculationStrategy {

    @Override
    public BigDecimal calculate(BigDecimal originalAmount, OrderContext context) {
        if (context.getDiscountStrategy() == null || context.getDiscountStrategy().getDiscountAmount() == null) {
            return BigDecimal.ZERO;
        }
        
        // 检查最低消费金额
        BigDecimal minAmount = context.getDiscountStrategy().getMinAmount();
        if (minAmount != null && originalAmount.compareTo(minAmount) < 0) {
            return BigDecimal.ZERO;
        }
        
        // 返回固定折扣金额，但不能超过原始金额
        BigDecimal discountAmount = context.getDiscountStrategy().getDiscountAmount();
        return discountAmount.min(originalAmount);
    }

    @Override
    public String getStrategyType() {
        return "FIXED_AMOUNT";
    }
} 
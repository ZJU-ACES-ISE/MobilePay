package org.software.code.strategy.impl;

import org.software.code.strategy.DiscountCalculationStrategy;
import org.software.code.vo.OrderContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 百分比折扣策略实现类
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Component
public class PercentageDiscountStrategy implements DiscountCalculationStrategy {

    @Override
    public BigDecimal calculate(BigDecimal originalAmount, OrderContext context) {
        if (context.getDiscountStrategy() == null || context.getDiscountStrategy().getDiscountRate() == null) {
            return BigDecimal.ZERO;
        }
        
        // 检查最低消费金额
        BigDecimal minAmount = context.getDiscountStrategy().getMinAmount();
        if (minAmount != null && originalAmount.compareTo(minAmount) < 0) {
            return BigDecimal.ZERO;
        }
        
        // 计算百分比折扣
        BigDecimal discountRate = context.getDiscountStrategy().getDiscountRate();
        BigDecimal discount = originalAmount.multiply(discountRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        
        // 应用最大折扣限制
        BigDecimal maxDiscount = context.getMaxDiscount();
        if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
            discount = maxDiscount;
        }
        
        return discount.min(originalAmount); // 折扣不能超过原始金额
    }

    @Override
    public String getStrategyType() {
        return "PERCENTAGE";
    }
} 
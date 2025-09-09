package org.software.code.strategy.impl;

import org.software.code.strategy.DiscountCalculationStrategy;
import org.software.code.vo.OrderContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 阶梯折扣策略实现类
 * 根据消费金额阶梯给予不同的折扣
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Component
public class LadderDiscountStrategy implements DiscountCalculationStrategy {

    @Override
    public BigDecimal calculate(BigDecimal originalAmount, OrderContext context) {
        if (context.getDiscountStrategy() == null) {
            return BigDecimal.ZERO;
        }
        
        // 检查最低消费金额
        BigDecimal minAmount = context.getDiscountStrategy().getMinAmount();
        if (minAmount != null && originalAmount.compareTo(minAmount) < 0) {
            return BigDecimal.ZERO;
        }
        
        // 阶梯折扣逻辑：根据金额范围给予不同折扣
        BigDecimal discount = calculateLadderDiscount(originalAmount, context);
        
        // 应用最大折扣限制
        BigDecimal maxDiscount = context.getMaxDiscount();
        if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
            discount = maxDiscount;
        }
        
        return discount.min(originalAmount); // 折扣不能超过原始金额
    }
    
    /**
     * 计算阶梯折扣
     * 这里实现一个简单的阶梯逻辑，实际项目中可能需要更复杂的规则配置
     */
    private BigDecimal calculateLadderDiscount(BigDecimal originalAmount, OrderContext context) {
        BigDecimal discount = BigDecimal.ZERO;
        
        // 示例阶梯规则：
        // 0-50元：无折扣
        // 50-100元：5%折扣
        // 100-200元：10%折扣
        // 200元以上：15%折扣
        
        if (originalAmount.compareTo(new BigDecimal("50")) > 0) {
            if (originalAmount.compareTo(new BigDecimal("100")) <= 0) {
                // 50-100元：5%折扣
                discount = originalAmount.multiply(new BigDecimal("0.05"));
            } else if (originalAmount.compareTo(new BigDecimal("200")) <= 0) {
                // 100-200元：10%折扣
                discount = originalAmount.multiply(new BigDecimal("0.10"));
            } else {
                // 200元以上：15%折扣
                discount = originalAmount.multiply(new BigDecimal("0.15"));
            }
        }
        
        return discount.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getStrategyType() {
        return "LADDER";
    }
} 
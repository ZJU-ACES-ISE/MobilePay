package org.software.code.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 折扣策略工厂类
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Component
public class DiscountStrategyFactory {
    
    @Autowired
    private List<DiscountCalculationStrategy> strategies;
    
    private Map<String, DiscountCalculationStrategy> strategyMap = new HashMap<>();
    
    @PostConstruct
    public void init() {
        for (DiscountCalculationStrategy strategy : strategies) {
            strategyMap.put(strategy.getStrategyType(), strategy);
        }
    }
    
    /**
     * 根据策略类型获取策略实现
     * 
     * @param strategyType 策略类型
     * @return 策略实现
     */
    public DiscountCalculationStrategy getStrategy(String strategyType) {
        return strategyMap.get(strategyType);
    }
} 
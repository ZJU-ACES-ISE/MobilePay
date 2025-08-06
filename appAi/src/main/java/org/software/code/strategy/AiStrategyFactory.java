package org.software.code.strategy;

import org.software.code.model.AiModel;
import org.software.code.model.OpenAiModel;
import org.software.code.model.DeepSeekModel;
import org.software.code.prompt.PromptStrategy;
import org.software.code.prompt.MonthlyOverviewStrategy;
import org.software.code.prompt.ConsumptionAnalysisStrategy;
import org.software.code.prompt.SpendingAlertStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * AI策略工厂
 */
@Component
public class AiStrategyFactory {
    
    private final Map<String, AiModel> aiModels = new HashMap<>();
    private final Map<String, PromptStrategy> promptStrategies = new HashMap<>();
    
    @Autowired
    public AiStrategyFactory(OpenAiModel openAiModel,
                             DeepSeekModel deepSeekModel,
                             MonthlyOverviewStrategy monthlyOverviewStrategy,
                             ConsumptionAnalysisStrategy consumptionAnalysisStrategy,
                             SpendingAlertStrategy spendingAlertStrategy) {
        // 初始化AI模型
        aiModels.put("OPENAI", openAiModel);
        aiModels.put("DEEPSEEK", deepSeekModel);
        
        // 初始化提示词策略
        promptStrategies.put("MONTHLY_OVERVIEW", monthlyOverviewStrategy);
        promptStrategies.put("CONSUMPTION_ANALYSIS", consumptionAnalysisStrategy);
        promptStrategies.put("SPENDING_ALERT", spendingAlertStrategy);
    }
    
    /**
     * 获取AI模型
     * @param modelType 模型类型
     * @return AI模型实例
     */
    public AiModel getAiModel(String modelType) {
        return aiModels.get(modelType.toUpperCase());
    }
    
    /**
     * 获取提示词策略
     * @param strategyType 策略类型
     * @return 提示词策略实例
     */
    public PromptStrategy getPromptStrategy(String strategyType) {
        return promptStrategies.get(strategyType.toUpperCase());
    }
    
    /**
     * 获取所有可用的AI模型类型
     * @return 模型类型列表
     */
    public String[] getAvailableAiModels() {
        return aiModels.keySet().toArray(new String[0]);
    }
    
    /**
     * 获取所有可用的提示词策略类型
     * @return 策略类型列表
     */
    public String[] getAvailablePromptStrategies() {
        return promptStrategies.keySet().toArray(new String[0]);
    }
} 
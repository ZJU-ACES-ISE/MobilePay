package org.software.code.prompt;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 消费分析与建议策略
 */
@Component
public class ConsumptionAnalysisStrategy implements PromptStrategy {
    
    @Override
    public String generatePrompt(Map<String, Object> context) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请对用户的消费行为进行深入分析，并提供个性化建议：\n");
        prompt.append("1. 消费结构分析\n");
        prompt.append("   - 必要支出vs非必要支出比例\n");
        prompt.append("   - 各消费类别的合理性评估\n");
        prompt.append("2. 消费效率分析\n");
        prompt.append("   - 是否存在重复或浪费的消费\n");
        prompt.append("   - 消费时机是否合理\n");
        prompt.append("3. 个性化建议\n");
        prompt.append("   - 预算优化建议\n");
        prompt.append("   - 消费习惯改进建议\n");
        prompt.append("   - 理财建议\n\n");
        prompt.append("用户消费数据：").append(context.get("transactionData")).append("\n");
        prompt.append("请提供具体、可操作的改进建议。");
        
        return prompt.toString();
    }
    
    @Override
    public String getStrategyType() {
        return "CONSUMPTION_ANALYSIS";
    }
} 
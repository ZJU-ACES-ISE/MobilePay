package org.software.code.prompt;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 本月消费概览策略
 */
@Component
public class MonthlyOverviewStrategy implements PromptStrategy {
    
    @Override
    public String generatePrompt(Map<String, Object> context) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请分析用户的月度消费情况，提供以下信息：\n");
        prompt.append("1. 本月总消费金额\n");
        prompt.append("2. 主要消费类别分布\n");
        prompt.append("3. 与上月的消费对比\n");
        prompt.append("4. 消费趋势分析\n");
        prompt.append("5. 消费习惯总结\n\n");
        prompt.append("用户消费数据：").append(context.get("transactionData")).append("\n");
        prompt.append("请用简洁明了的语言进行分析，并提供实用的建议。");
        
        return prompt.toString();
    }
    
    @Override
    public String getStrategyType() {
        return "MONTHLY_OVERVIEW";
    }
} 
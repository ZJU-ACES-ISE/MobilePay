package org.software.code.prompt;

import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 高频/异常支出提醒策略
 */
@Component
public class SpendingAlertStrategy implements PromptStrategy {
    
    @Override
    public String generatePrompt(Map<String, Object> context) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请分析用户的消费模式，识别并提醒以下情况：\n");
        prompt.append("1. 高频消费识别\n");
        prompt.append("   - 频繁消费的商家或类别\n");
        prompt.append("   - 消费频率异常的服务\n");
        prompt.append("2. 异常支出检测\n");
        prompt.append("   - 超出正常范围的消费金额\n");
        prompt.append("   - 不寻常的消费时间或地点\n");
        prompt.append("   - 与历史消费模式不符的交易\n");
        prompt.append("3. 风险提醒\n");
        prompt.append("   - 可能的冲动消费\n");
        prompt.append("   - 潜在的消费陷阱\n");
        prompt.append("   - 预算超支风险\n\n");
        prompt.append("用户消费数据：").append(context.get("transactionData")).append("\n");
        prompt.append("请重点关注异常模式，并提供风险控制建议。");
        
        return prompt.toString();
    }
    
    @Override
    public String getStrategyType() {
        return "SPENDING_ALERT";
    }
} 
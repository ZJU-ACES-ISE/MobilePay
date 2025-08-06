package org.software.code.prompt;

import java.util.Map;

/**
 * 提示词策略接口
 */
public interface PromptStrategy {
    
    /**
     * 生成提示词
     * @param context 上下文数据
     * @return 生成的提示词
     */
    String generatePrompt(Map<String, Object> context);
    
    /**
     * 获取策略类型
     * @return 策略类型
     */
    String getStrategyType();
} 
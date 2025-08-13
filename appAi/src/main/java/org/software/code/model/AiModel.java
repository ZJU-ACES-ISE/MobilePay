package org.software.code.model;

/**
 * AI模型接口 - 策略模式
 */
public interface AiModel {
    
    /**
     * 发送请求到AI模型
     * @param prompt 提示词
     * @param context 上下文信息
     * @return AI响应结果
     */
    String sendRequest(String prompt, String context);
    
    /**
     * 获取模型名称
     * @return 模型名称
     */
    String getModelName();
} 
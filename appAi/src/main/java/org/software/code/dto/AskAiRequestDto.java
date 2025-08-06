package org.software.code.dto;

import lombok.Data;

/**
 * AI请求DTO
 */
@Data
public class AskAiRequestDto {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * AI模型类型 (OPENAI, DEEPSEEK)
     */
    private String modelType;
    
    /**
     * 分析策略类型 (MONTHLY_OVERVIEW, CONSUMPTION_ANALYSIS, SPENDING_ALERT)
     */
    private String strategyType;
    
    /**
     * 分析时间范围 (可选，默认当前月)
     */
    private String timeRange;
    
    /**
     * 自定义参数
     */
    private String customParams;
} 
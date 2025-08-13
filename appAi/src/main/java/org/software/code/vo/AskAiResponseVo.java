package org.software.code.vo;

import lombok.Data;

/**
 * AI响应VO
 */
@Data
public class AskAiResponseVo {
    
    /**
     * 分析结果
     */
    private String analysisResult;
    
    /**
     * 使用的AI模型
     */
    private String usedModel;
    
    /**
     * 使用的策略类型
     */
    private String strategyType;
    
    /**
     * 分析时间
     */
    private String analysisTime;
    
    /**
     * 数据范围
     */
    private String dataRange;
} 
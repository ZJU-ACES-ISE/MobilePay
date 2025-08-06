package org.software.code.ai.template.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.software.code.ai.template.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 提示词模板实现类
 */
@Slf4j
@Component
public class PromptTemplateImpl implements PromptTemplate {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public String getPrompt(String sceneType, Map<String, Object> params) {
        try {
            // 根据场景类型选择不同的提示词模板
            switch (sceneType.toUpperCase()) {
                case "MONTHLY_SUMMARY":
                    return buildMonthlySummaryPrompt(params);
                case "SPENDING_ANALYSIS":
                    return buildSpendingAnalysisPrompt(params);
                case "ABNORMAL_SPENDING":
                    return buildAbnormalSpendingPrompt(params);
                default:
                    return buildDefaultPrompt(sceneType, params);
            }
        } catch (Exception e) {
            log.error("构建提示词异常", e);
            return "请分析以下消费数据：" + safeSerialize(params);
        }
    }
    
    private String buildMonthlySummaryPrompt(Map<String, Object> params) {
        return "你是一个专业的财务顾问。请根据以下用户消费数据，生成一份本月消费概览报告。" +
                "报告应包含以下内容：\n" +
                "1. 本月总支出金额\n" +
                "2. 各消费类别占比（使用饼图描述）\n" +
                "3. 与上个月的消费对比\n" +
                "4. 消费趋势分析\n\n" +
                "消费数据：" + safeSerialize(params) + "\n\n" +
                "请使用清晰、简洁的语言，并适当使用emoji增加可读性。";
    }
    
    private String buildSpendingAnalysisPrompt(Map<String, Object> params) {
        return "你是一个专业的财务分析师。请根据以下用户的消费记录，提供详细的消费分析与建议。\n\n" +
                "分析要求：\n" +
                "1. 识别主要消费类别和金额\n" +
                "2. 分析消费习惯（如高频消费、大额消费等）\n" +
                "3. 提供优化建议\n" +
                "4. 如发现异常消费模式，请特别指出\n\n" +
                "消费数据：" + safeSerialize(params) + "\n\n" +
                "请使用专业但易于理解的语言，并提供具体的建议。";
    }
    
    private String buildAbnormalSpendingPrompt(Map<String, Object> params) {
        return "你是一个专业的风险控制专家。请分析以下用户的消费记录，识别并报告任何异常或高频支出。\n\n" +
                "分析要求：\n" +
                "1. 识别异常交易（如大额、高频、非常规时间等）\n" +
                "2. 评估每笔异常交易的风险等级（高/中/低）\n" +
                "3. 提供风险说明\n" +
                "4. 如发现可疑活动，建议采取的措施\n\n" +
                "消费数据：" + safeSerialize(params) + "\n\n" +
                "请使用清晰、简洁的语言，对高风险项目进行突出显示。";
    }
    
    private String buildDefaultPrompt(String sceneType, Map<String, Object> params) {
        return String.format("请根据以下数据执行'%s'分析：\n\n%s", 
                sceneType, 
                safeSerialize(params));
    }
    
    private String safeSerialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败", e);
            return obj != null ? obj.toString() : "null";
        }
    }
}

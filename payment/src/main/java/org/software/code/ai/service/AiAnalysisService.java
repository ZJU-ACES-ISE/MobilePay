package org.software.code.ai.service;

import org.software.code.ai.mapper.TransactionRecordMapper;
import org.software.code.ai.model.AnalysisRequest;
import org.software.code.ai.model.AnalysisResult;
import org.software.code.ai.strategy.AiStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 分析服务
 */
@Service
public class AiAnalysisService {
    
    @Autowired
    private List<AiStrategy> strategies;
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private TransactionRecordMapper transactionRecordMapper;
    
    /**
     * 执行 AI 分析
     * @param request 分析请求
     * @return 分析结果
     */
    public AnalysisResult analyze(AnalysisRequest request) {
        try {
            // 准备分析数据
            Map<String, Object> analysisData = prepareAnalysisData(request);
            request.setParams(analysisData);
            
            // 如果未指定提供方，默认为 openai
            String provider = request.getAiProvider() != null ? request.getAiProvider() : "openai";
            
            // 查找支持该提供方的策略
            return strategies.stream()
                    .filter(strategy -> strategy.supports(provider))
                    .findFirst()
                    .map(strategy -> strategy.analyze(request))
                    .orElseGet(() -> AnalysisResult.error("不支持的 AI 提供方: " + provider));
        } catch (Exception e) {
            return AnalysisResult.error("分析过程中发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 准备分析数据
     */
    private Map<String, Object> prepareAnalysisData(AnalysisRequest request) {
        Map<String, Object> data = new HashMap<>();
        Long userId = request.getUserId();
        
        switch (request.getSceneType().toUpperCase()) {
            case "MONTHLY_SUMMARY":
                data.putAll(transactionService.getMonthlyTransactions(userId));
                break;
                
            case "SPENDING_ANALYSIS":
                // 获取最近3个月的交易记录
                data.putAll(transactionService.getMonthlyTransactions(userId));
                // 可以添加更多分析数据
                break;
                
            case "ABNORMAL_SPENDING":
                data.put("abnormalTransactions", transactionService.getAbnormalTransactions(userId));
                break;
                
            default:
                // 默认获取最近一个月的交易记录
                data.putAll(transactionService.getMonthlyTransactions(userId));
        }
        
        // 添加用户ID和场景类型
        data.put("userId", userId);
        data.put("sceneType", request.getSceneType());
        
        return data;
    }
}

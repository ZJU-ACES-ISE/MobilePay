package org.software.code.ai.controller;

import org.software.code.ai.model.AnalysisRequest;
import org.software.code.ai.model.AnalysisResult;
import org.software.code.ai.service.AiAnalysisService;
import org.software.code.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * AI 分析控制器
 */
@RestController
@RequestMapping("/api/ai")
public class AiAnalysisController {
    
    @Autowired
    private AiAnalysisService aiAnalysisService;
    
    /**
     * AI 分析接口
     * @param request 分析请求
     * @return 分析结果
     */
    @PostMapping("/analyze")
    public Result<AnalysisResult> analyze(@Valid @RequestBody AnalysisRequest request) {
        AnalysisResult result = aiAnalysisService.analyze(request);
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error(result.getErrorMessage());
        }
    }
    
    /**
     * 获取本月消费概览
     */
    @GetMapping("/monthly-summary")
    public Result<AnalysisResult> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam(required = false) String aiProvider) {
        AnalysisRequest request = new AnalysisRequest();
        request.setUserId(userId);
        request.setSceneType("MONTHLY_SUMMARY");
        if (aiProvider != null) {
            request.setAiProvider(aiProvider);
        }
        return analyze(request);
    }
    
    /**
     * 获取消费分析与建议
     */
    @GetMapping("/spending-analysis")
    public Result<AnalysisResult> getSpendingAnalysis(
            @RequestParam Long userId,
            @RequestParam(required = false) String aiProvider) {
        AnalysisRequest request = new AnalysisRequest();
        request.setUserId(userId);
        request.setSceneType("SPENDING_ANALYSIS");
        if (aiProvider != null) {
            request.setAiProvider(aiProvider);
        }
        return analyze(request);
    }
    
    /**
     * 获取异常消费提醒
     */
    @GetMapping("/abnormal-spending")
    public Result<AnalysisResult> getAbnormalSpending(
            @RequestParam Long userId,
            @RequestParam(required = false) String aiProvider) {
        AnalysisRequest request = new AnalysisRequest();
        request.setUserId(userId);
        request.setSceneType("ABNORMAL_SPENDING");
        if (aiProvider != null) {
            request.setAiProvider(aiProvider);
        }
        return analyze(request);
    }
}

package org.software.code.controller;

import org.software.code.common.result.ResultEnum;
import org.software.code.dto.AskAiRequestDto;
import org.software.code.service.AiService;
import org.software.code.vo.AskAiResponseVo;
import org.software.code.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AI控制器
 */
@RestController
@RequestMapping("/ai")
public class AiController {
    
    @Autowired
    private AiService aiService;
    
    /**
     * AI分析接口
     * @param requestDto 请求参数
     * @return AI分析结果
     */
    @PostMapping("/ask")
    public Result<AskAiResponseVo> askAi(@RequestBody AskAiRequestDto requestDto) {
        try {
            AskAiResponseVo responseVo = aiService.askAi(requestDto);
            return Result.success(responseVo);
        } catch (Exception e) {
            return Result.failed("AI分析失败: " + e.getMessage(),AskAiResponseVo.class);
        }
    }
    
    /**
     * 获取可用的AI模型列表
     * @return 模型列表
     */
    @GetMapping("/models")
    public Result<String[]> getAvailableModels() {
        try {
            // 这里可以从AiStrategyFactory获取可用模型
            String[] models = {"OPENAI", "DEEPSEEK"};
            return Result.success(models);
        } catch (Exception e) {
            return Result.instance(ResultEnum.FAILED.getCode(),"获取模型列表失败: " + e.getMessage(),null);
        }
    }
    
    /**
     * 获取可用的策略列表
     * @return 策略列表
     */
    @GetMapping("/strategies")
    public Result<String[]> getAvailableStrategies() {
        try {
            // 这里可以从AiStrategyFactory获取可用策略
            String[] strategies = {"MONTHLY_OVERVIEW", "CONSUMPTION_ANALYSIS", "SPENDING_ALERT"};
            return Result.success(strategies);
        } catch (Exception e) {
            return Result.instance(ResultEnum.FAILED.getCode(),"获取策略列表失败: " + e.getMessage(),null);
        }
    }
} 
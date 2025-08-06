package org.software.code.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.dto.AskAiRequestDto;
import org.software.code.entity.TransactionRecord;
import org.software.code.mapper.TransactionRecordMapper;
import org.software.code.model.AiModel;
import org.software.code.prompt.PromptStrategy;
import org.software.code.service.AiService;
import org.software.code.strategy.AiStrategyFactory;
import org.software.code.vo.AskAiResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI服务实现
 */
@Service
public class AiServiceImpl implements AiService {
    
    @Autowired
    private AiStrategyFactory aiStrategyFactory;
    
    @Autowired
    private TransactionRecordMapper transactionRecordMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public AskAiResponseVo askAi(AskAiRequestDto requestDto) {
        try {
            // 1. 获取用户交易数据
            String transactionData = getUserTransactionData(requestDto.getUserId(), requestDto.getTimeRange());
            
            // 2. 获取AI模型
            AiModel aiModel = aiStrategyFactory.getAiModel(requestDto.getModelType());
            if (aiModel == null) {
                throw new RuntimeException("不支持的AI模型类型: " + requestDto.getModelType());
            }
            
            // 3. 获取提示词策略
            PromptStrategy promptStrategy = aiStrategyFactory.getPromptStrategy(requestDto.getStrategyType());
            if (promptStrategy == null) {
                throw new RuntimeException("不支持的策略类型: " + requestDto.getStrategyType());
            }
            
            // 4. 构建上下文数据
            Map<String, Object> context = new HashMap<>();
            context.put("transactionData", transactionData);
            context.put("userId", requestDto.getUserId());
            context.put("timeRange", requestDto.getTimeRange());
            context.put("customParams", requestDto.getCustomParams());
            
            // 5. 生成提示词
            String prompt = promptStrategy.generatePrompt(context);
            
            // 6. 调用AI模型
            String aiResponse = aiModel.sendRequest(prompt, transactionData);
            
            // 7. 构建响应
            AskAiResponseVo responseVo = new AskAiResponseVo();
            responseVo.setAnalysisResult(aiResponse);
            responseVo.setUsedModel(aiModel.getModelName());
            responseVo.setStrategyType(promptStrategy.getStrategyType());
            responseVo.setAnalysisTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            responseVo.setDataRange(requestDto.getTimeRange());
            
            return responseVo;
            
        } catch (Exception e) {
            throw new RuntimeException("AI分析失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String getUserTransactionData(Long userId, String timeRange) {
        try {
            // 解析时间范围，默认查询当前月
            LocalDateTime startTime, endTime;
            if (timeRange == null || timeRange.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                startTime = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                endTime = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            } else {
                // TODO: 根据timeRange解析具体的时间范围
                LocalDateTime now = LocalDateTime.now();
                startTime = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                endTime = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            }
            
            // 查询交易记录
            List<TransactionRecord> records = transactionRecordMapper.selectByUserIdAndTimeRange(userId, startTime, endTime);
            
            // 转换为JSON字符串
            return objectMapper.writeValueAsString(records);
            
        } catch (Exception e) {
            throw new RuntimeException("获取用户交易数据失败: " + e.getMessage(), e);
        }
    }
} 
package org.software.code.ai.strategy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.software.code.ai.model.AnalysisRequest;
import org.software.code.ai.model.AnalysisResult;
import org.software.code.ai.strategy.AiStrategy;
import org.software.code.ai.template.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * DeepSeek 策略实现
 */
@Slf4j
@Component
public class DeepSeekStrategy implements AiStrategy {
    
    @Value("${ai.deepseek.api-key}")
    private String apiKey;
    
    @Value("${ai.deepseek.endpoint:https://api.deepseek.com/v1/chat/completions}")
    private String endpoint;
    
    @Value("${ai.deepseek.model:deepseek-chat}")
    private String defaultModel;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PromptTemplate promptTemplate;
    
    @Override
    public AnalysisResult analyze(AnalysisRequest request) {
        try {
            // 1. 获取提示词模板
            String prompt = promptTemplate.getPrompt(request.getSceneType(), request.getParams());
            
            // 2. 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", request.getModelName() != null ? request.getModelName() : defaultModel);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            requestBody.put("messages", new Map[]{message});
            
            // 3. 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            // 4. 发送请求
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    endpoint, HttpMethod.POST, entity, String.class);
            
            // 5. 解析响应
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
                String content = extractContentFromResponse(responseMap);
                return AnalysisResult.success(
                        content, 
                        "deepseek", 
                        request.getModelName() != null ? request.getModelName() : defaultModel
                );
            } else {
                log.error("DeepSeek API 调用失败: {}", response.getBody());
                return AnalysisResult.error("DeepSeek API 调用失败: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("DeepSeek 分析异常", e);
            return AnalysisResult.error("DeepSeek 分析异常: " + e.getMessage());
        }
    }
    
    @Override
    public boolean supports(String provider) {
        return "deepseek".equalsIgnoreCase(provider);
    }
    
    @SuppressWarnings("unchecked")
    private String extractContentFromResponse(Map<String, Object> response) {
        try {
            return ((Map<String, Object>)((Map<String, Object>)((java.util.List<?>)response.get("choices")).get(0))
                    .get("message"))
                    .get("content").toString();
        } catch (Exception e) {
            log.error("解析 DeepSeek 响应失败", e);
            return "无法解析 AI 响应";
        }
    }
}

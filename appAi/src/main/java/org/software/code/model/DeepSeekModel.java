package org.software.code.model;

import org.springframework.stereotype.Component;

/**
 * DeepSeek模型实现
 */
@Component
public class DeepSeekModel implements AiModel {
    
    @Override
    public String sendRequest(String prompt, String context) {
        // TODO: 实现DeepSeek API调用
        // 这里应该调用DeepSeek的API
        return "DeepSeek响应: " + prompt;
    }
    
    @Override
    public String getModelName() {
        return "DeepSeek";
    }
} 
package org.software.code.model;

import org.springframework.stereotype.Component;

/**
 * OpenAI模型实现
 */
@Component
public class OpenAiModel implements AiModel {
    
    @Override
    public String sendRequest(String prompt, String context) {
        // TODO: 实现OpenAI API调用
        // 这里应该调用OpenAI的API
        return "OpenAI响应: " + prompt;
    }
    
    @Override
    public String getModelName() {
        return "OpenAI";
    }
} 
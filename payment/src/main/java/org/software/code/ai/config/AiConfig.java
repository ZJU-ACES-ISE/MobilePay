package org.software.code.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AI 模块配置类
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {
    
    private OpenAiConfig openai;
    private DeepSeekConfig deepseek;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    // Getters and Setters
    public OpenAiConfig getOpenai() {
        return openai;
    }
    
    public void setOpenai(OpenAiConfig openai) {
        this.openai = openai;
    }
    
    public DeepSeekConfig getDeepseek() {
        return deepseek;
    }
    
    public void setDeepseek(DeepSeekConfig deepseek) {
        this.deepseek = deepseek;
    }
    
    /**
     * OpenAI 配置
     */
    public static class OpenAiConfig {
        private String apiKey;
        private String endpoint;
        private String model;
        
        // Getters and Setters
        public String getApiKey() {
            return apiKey;
        }
        
        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public String getModel() {
            return model;
        }
        
        public void setModel(String model) {
            this.model = model;
        }
    }
    
    /**
     * DeepSeek 配置
     */
    public static class DeepSeekConfig {
        private String apiKey;
        private String endpoint;
        private String model;
        
        // Getters and Setters
        public String getApiKey() {
            return apiKey;
        }
        
        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public String getModel() {
            return model;
        }
        
        public void setModel(String model) {
            this.model = model;
        }
    }
}

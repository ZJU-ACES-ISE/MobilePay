package org.software.code.ai.template;

import java.util.Map;

/**
 * 提示词模板接口
 */
public interface PromptTemplate {
    
    /**
     * 获取指定场景的提示词
     * @param sceneType 场景类型
     * @param params 参数
     * @return 提示词内容
     */
    String getPrompt(String sceneType, Map<String, Object> params);
}

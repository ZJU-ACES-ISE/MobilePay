package org.software.code.service;

import org.software.code.dto.AskAiRequestDto;
import org.software.code.vo.AskAiResponseVo;

/**
 * AI服务接口
 */
public interface AiService {
    
    /**
     * 请求AI分析
     * @param requestDto 请求参数
     * @return AI分析结果
     */
    AskAiResponseVo askAi(AskAiRequestDto requestDto);
    
    /**
     * 获取用户交易数据
     * @param userId 用户ID
     * @param timeRange 时间范围
     * @return 交易数据JSON字符串
     */
    String getUserTransactionData(Long userId, String timeRange);
} 
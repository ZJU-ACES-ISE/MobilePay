package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 补缴响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitRepayResponseVo {
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 清除时间
     */
    private String clearedAt;
    
    /**
     * 交易ID
     */
    private String transcationId;
} 
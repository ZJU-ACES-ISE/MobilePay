package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 出站响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitExitResponseVo {
    
    /**
     * 出站记录ID
     */
    private String transitId;
    
    /**
     * 出行方式
     */
    private String mode;
    
    /**
     * 出站状态（-1=未出站，0=出行正常，1=支付异常，2=出行异常）
     */
    private Integer status;
    
    /**
     * 出站异常原因（status是1或2的情况下）
     */
    private String reason;
    
    /**
     * 出站费用（status是0的情况下）
     */
    private String fee;
    
    /**
     * 持续时间（status是0的情况下）
     */
    private String duration;
    
    /**
     * 交易记录ID（status是0的情况下）
     */
    private String transcationId;
} 
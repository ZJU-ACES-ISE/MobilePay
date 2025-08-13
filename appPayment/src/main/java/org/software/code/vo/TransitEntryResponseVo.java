package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 进站响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitEntryResponseVo {
    
    /**
     * 出行ID
     */
    private String transitId;
    
    /**
     * 出行方式
     */
    private String mode;
    
    /**
     * 进站点
     */
    private String entryStation;
    
    /**
     * 进站时间
     */
    private LocalDateTime entryTime;
    
    /**
     * 进站站点线路
     */
    private String entryLine;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 状态（0:正常, 1:异常）
     */
    private Integer status;
} 
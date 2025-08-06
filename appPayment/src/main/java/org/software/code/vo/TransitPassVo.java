package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通行码响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitPassVo {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 城市ID
     */
    private String cityId;
    
    /**
     * 城市名称
     */
    private String cityName;
    
    /**
     * 通行码URL
     */
    private String codeUrl;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
} 
package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 城市信息响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityVo {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 城市ID
     */
    private String cityId;
    
    /**
     * 城市名称
     */
    private String cityName;
} 
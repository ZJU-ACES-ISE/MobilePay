package org.software.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 通行码请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitPassRequestDto {
    
    /**
     * 城市ID
     */
    @NotBlank(message = "城市ID不能为空")
    private String cityId;
    
    /**
     * 城市名称
     */
    @NotBlank(message = "城市名称不能为空")
    private String cityName;
} 
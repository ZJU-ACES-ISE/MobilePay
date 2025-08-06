package org.software.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 出站请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitExitRequestDto {
    
    /**
     * 出站点
     */
    @NotBlank(message = "出站点不能为空")
    private String exitStation;
    
    /**
     * 出站时间
     */
    @NotBlank(message = "出站时间不能为空")
    private String exitTime;
    
    /**
     * 出行方式
     */
    @NotBlank(message = "出行方式不能为空")
    private String mode;
} 
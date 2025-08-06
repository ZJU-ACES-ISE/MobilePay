package org.software.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 进站请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitEntryRequestDto {
    
    /**
     * 进站点
     */
    @NotBlank(message = "进站点不能为空")
    private String entryStation;
    
    /**
     * 进站时间
     */
    @NotBlank(message = "进站时间不能为空")
    private String entryTime;
    
    /**
     * 出行方式
     */
    @NotBlank(message = "出行方式不能为空")
    private String mode;
} 
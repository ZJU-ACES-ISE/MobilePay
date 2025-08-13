package org.software.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 补缴请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitRepayRequestDto {
    
    /**
     * 出行ID
     */
    @NotBlank(message = "出行ID不能为空")
    private String transitId;
    
    /**
     * 支付金额
     */
    @NotBlank(message = "支付金额不能为空")
    private String amount;
    
    /**
     * 支付时间
     */
    @NotBlank(message = "支付时间不能为空")
    private String payTime;
    
    /**
     * 交易记录ID
     */
    @NotBlank(message = "交易记录ID不能为空")
    private String transcationId;
} 
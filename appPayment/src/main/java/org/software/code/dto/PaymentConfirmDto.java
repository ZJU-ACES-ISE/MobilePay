package org.software.code.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 支付确认请求DTO
 */
@Data
public class PaymentConfirmDto {
    
    /**
     * 交易对象ID（如商户或银行卡）
     */
    @NotNull(message = "交易对象ID不能为空")
    private Long targetId;
    
    /**
     * 交易对象名称
     */
    @NotBlank(message = "交易对象名称不能为空")
    private String targetName;
    
    /**
     * 交易对象类型：1=用户，2=商户，3=银行卡
     */
    @NotNull(message = "交易对象类型不能为空")
    private Integer targetType;
    
    /**
     * 实付金额
     */
    @NotBlank(message = "实付金额不能为空")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "金额格式不正确")
    private String actualAmount;
    
    /**
     * 交易类型：1=收入，2=转出
     */
    @NotNull(message = "交易类型不能为空")
    private Integer type;
    
    /**
     * 支出分类（type=2时必填）
     */
    private Integer bizCategory;
} 
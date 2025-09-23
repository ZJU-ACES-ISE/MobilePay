package org.software.code.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 新增折扣策略请求DTO
 */
@Data
public class DiscountStrategyCreateDto {
    
    /**
     * 策略名称
     */
    @NotBlank(message = "策略名称不能为空")
    private String strategyName;
    
    /**
     * 策略类型：TRAVEL, PAYMENT, NEW_USER, HOLIDAY
     */
    @NotBlank(message = "策略类型不能为空")
    @Pattern(regexp = "^(TRAVEL|PAYMENT|NEW_USER|HOLIDAY)$", message = "策略类型必须是TRAVEL、PAYMENT、NEW_USER或HOLIDAY")
    private String strategyType;
    
    /**
     * 折扣类型：PERCENTAGE, FIXED_AMOUNT, LADDER
     */
    @NotBlank(message = "折扣类型不能为空")
    @Pattern(regexp = "^(PERCENTAGE|FIXED_AMOUNT|LADDER)$", message = "折扣类型必须是PERCENTAGE、FIXED_AMOUNT或LADDER")
    private String discountType;
    
    /**
     * 折扣率（百分比，0-100）
     */
    @DecimalMin(value = "0", message = "折扣率不能小于0")
    private BigDecimal discountRate;
    
    /**
     * 折扣金额
     */
    @DecimalMin(value = "0", message = "折扣金额不能小于0")
    private BigDecimal discountAmount;
    
    /**
     * 最低消费金额
     */
    @DecimalMin(value = "0", message = "最低消费金额不能小于0")
    private BigDecimal minAmount;
    
    /**
     * 最大折扣金额
     */
    @DecimalMin(value = "0", message = "最大折扣金额不能小于0")
    private BigDecimal maxDiscount;
    
    /**
     * 目标城市（JSON数组格式）
     */
    private String targetCities;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 使用限制次数
     */
    private Integer usageLimit;
} 
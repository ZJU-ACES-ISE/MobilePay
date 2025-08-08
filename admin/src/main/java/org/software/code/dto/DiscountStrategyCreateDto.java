package org.software.code.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DiscountStrategyCreateDto 是折扣策略创建请求数据传输对象，用于封装创建折扣策略的请求信息
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Data
@Getter
public class DiscountStrategyCreateDto {

    /**
     * 策略名称
     */
    @NotBlank(message = "策略名称不能为空")
    @Size(max = 100, message = "策略名称长度不能超过100字符")
    private String strategyName;

    /**
     * 策略编码（唯一）
     */
    @NotBlank(message = "策略编码不能为空")
    @Pattern(regexp = "^[A-Z0-9_]{4,20}$", message = "策略编码必须是4-20位字母、数字或下划线组合")
    private String strategyCode;

    /**
     * 策略类型：TRAVEL, PAYMENT, NEW_USER, HOLIDAY
     */
    @NotBlank(message = "策略类型不能为空")
    @Pattern(regexp = "^(TRAVEL|PAYMENT|NEW_USER|HOLIDAY)$", message = "策略类型只能是TRAVEL、PAYMENT、NEW_USER或HOLIDAY")
    private String strategyType;

    /**
     * 折扣类型：PERCENTAGE, FIXED_AMOUNT, LADDER
     */
    @NotBlank(message = "折扣类型不能为空")
    @Pattern(regexp = "^(PERCENTAGE|FIXED_AMOUNT|LADDER)$", message = "折扣类型只能是PERCENTAGE、FIXED_AMOUNT或LADDER")
    private String discountType;

    /**
     * 策略描述
     */
    @Size(max = 500, message = "策略描述长度不能超过500字符")
    private String description;

    /**
     * 折扣率（百分比，如0.10表示10%）
     */
    @DecimalMin(value = "0.00", message = "折扣率不能为负数")
    @DecimalMax(value = "1.00", message = "折扣率不能超过100%")
    private BigDecimal discountRate;

    /**
     * 折扣金额（固定金额）
     */
    @DecimalMin(value = "0.00", message = "折扣金额不能为负数")
    private BigDecimal discountAmount;

    /**
     * 最低消费金额
     */
    @DecimalMin(value = "0.00", message = "最低消费金额不能为负数")
    private BigDecimal minAmount;

    /**
     * 最大折扣金额
     */
    @DecimalMin(value = "0.00", message = "最大折扣金额不能为负数")
    private BigDecimal maxDiscount;

    /**
     * 目标用户类型：ALL, NEW, VIP, NORMAL
     */
    @Pattern(regexp = "^(ALL|NEW|VIP|NORMAL)$", message = "目标用户类型只能是ALL、NEW、VIP或NORMAL")
    private String targetUserType = "ALL";

    /**
     * 目标城市列表
     */
    private List<String> targetCities;

    /**
     * 目标站点ID列表
     */
    private List<Long> targetSites;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 使用限制次数（总次数）
     */
    @Min(value = 1, message = "使用限制次数必须大于0")
    private Integer usageLimit;

    /**
     * 每用户使用限制次数
     */
    @Min(value = 1, message = "每用户使用限制次数必须大于0")
    private Integer userUsageLimit;

    /**
     * 优先级（数字越大优先级越高）
     */
    @Min(value = 1, message = "优先级必须大于0")
    @Max(value = 999, message = "优先级不能超过999")
    private Integer priority = 100;

    /**
     * 是否可叠加使用
     */
    private Boolean stackable = false;
}

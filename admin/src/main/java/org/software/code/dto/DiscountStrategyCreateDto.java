package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
public class DiscountStrategyCreateDto {

    /**
     * 策略名称
     */
    @NotBlank(message = "策略名称不能为空")
    @Size(max = 100, message = "策略名称长度不能超过100字符")
    @JsonProperty("strategy_name")
    private String strategyName;

    /**
     * 策略编码（唯一）
     */
    @NotBlank(message = "策略编码不能为空")
    @Pattern(regexp = "^[A-Z0-9_]{4,20}$", message = "策略编码必须是4-20位字母、数字或下划线组合")
    @JsonProperty("strategy_code")
    private String strategyCode;

    /**
     * 策略类型：TRAVEL, PAYMENT, NEW_USER, HOLIDAY
     */
    @NotBlank(message = "策略类型不能为空")
    @Pattern(regexp = "^(TRAVEL|PAYMENT|NEW_USER|HOLIDAY)$", message = "策略类型只能是TRAVEL、PAYMENT、NEW_USER或HOLIDAY")
    @JsonProperty("strategy_type")
    private String strategyType;

    /**
     * 折扣类型：PERCENTAGE, FIXED_AMOUNT, LADDER
     */
    @NotBlank(message = "折扣类型不能为空")
    @Pattern(regexp = "^(PERCENTAGE|FIXED_AMOUNT|LADDER)$", message = "折扣类型只能是PERCENTAGE、FIXED_AMOUNT或LADDER")
    @JsonProperty("discount_type")
    private String discountType;

    /**
     * 策略描述
     */
    @Size(max = 500, message = "策略描述长度不能超过500字符")
    @JsonProperty("description")
    private String description;

    /**
     * 折扣率（百分比，如0.10表示10%）
     */
    @DecimalMin(value = "0.00", message = "折扣率不能为负数")
    @DecimalMax(value = "1.00", message = "折扣率不能超过100%")
    @JsonProperty("discount_rate")
    private BigDecimal discountRate;

    /**
     * 折扣金额（固定金额）
     */
    @DecimalMin(value = "0.00", message = "折扣金额不能为负数")
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    /**
     * 最低消费金额
     */
    @DecimalMin(value = "0.00", message = "最低消费金额不能为负数")
    @JsonProperty("min_amount")
    private BigDecimal minAmount;

    /**
     * 最大折扣金额
     */
    @DecimalMin(value = "0.00", message = "最大折扣金额不能为负数")
    @JsonProperty("max_discount")
    private BigDecimal maxDiscount;

    /**
     * 目标用户类型：ALL, NEW, VIP, NORMAL
     */
    @Pattern(regexp = "^(ALL|NEW|VIP|NORMAL)$", message = "目标用户类型只能是ALL、NEW、VIP或NORMAL")
    @JsonProperty("target_user_type")
    private String targetUserType = "ALL";

    /**
     * 目标城市列表
     */
    @JsonProperty("target_cities")
    private List<String> targetCities;

    /**
     * 目标站点ID列表
     */
    @JsonProperty("target_sites")
    private List<Long> targetSites;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @JsonProperty("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonProperty("end_time")
    private LocalDateTime endTime;

    /**
     * 使用限制次数（总次数）
     */
    @Min(value = 1, message = "使用限制次数必须大于0")
    @JsonProperty("usage_limit")
    private Integer usageLimit;

    /**
     * 每用户使用限制次数
     */
    @Min(value = 1, message = "每用户使用限制次数必须大于0")
    @JsonProperty("user_usage_limit")
    private Integer userUsageLimit;

    /**
     * 优先级（数字越大优先级越高）
     */
    @Min(value = 1, message = "优先级必须大于0")
    @Max(value = 999, message = "优先级不能超过999")
    @JsonProperty("priority")
    private Integer priority = 100;

    /**
     * 是否可叠加使用
     */
    @JsonProperty("stackable")
    private Boolean stackable = false;

    // 手动添加 getter 方法以确保可用
    public String getStrategyName() {
        return strategyName;
    }

    public String getStrategyCode() {
        return strategyCode;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public String getTargetUserType() {
        return targetUserType;
    }

    public List<String> getTargetCities() {
        return targetCities;
    }

    public List<Long> getTargetSites() {
        return targetSites;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public Integer getUserUsageLimit() {
        return userUsageLimit;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean getStackable() {
        return stackable;
    }
}

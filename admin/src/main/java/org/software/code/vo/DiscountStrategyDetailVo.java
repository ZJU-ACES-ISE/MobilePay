package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DiscountStrategyDetailVo 是折扣策略详情视图对象，用于封装折扣策略详细信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountStrategyDetailVo {

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 策略类型：TRAVEL（出行）、PAYMENT（支付）、NEW_USER（新用户）、HOLIDAY（节假日）
     */
    private String strategyType;

    /**
     * 策略类型描述
     */
    private String strategyTypeDescription;

    /**
     * 折扣类型：PERCENTAGE（百分比）、FIXED_AMOUNT（固定金额）、LADDER（阶梯）
     */
    private String discountType;

    /**
     * 折扣类型描述
     */
    private String discountTypeDescription;

    /**
     * 折扣率（百分比）
     */
    private BigDecimal discountRate;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 最低消费金额
     */
    private BigDecimal minAmount;

    /**
     * 最大折扣金额
     */
    private BigDecimal maxDiscount;

    /**
     * 目标城市列表（JSON数组格式）
     */
    private String targetCities;

    /**
     * 目标城市列表（解析后的字符串列表）
     */
    private List<String> targetCitiesList;

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

    /**
     * 已使用次数
     */
    private Integer usedCount;

    /**
     * 剩余可用次数
     */
    private Integer remainingCount;

    /**
     * 状态：ACTIVE（激活）、INACTIVE（停用）、EXPIRED（过期）
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDescription;

    /**
     * 创建者管理员ID
     */
    private Long createdBy;

    /**
     * 创建者管理员名称
     */
    private String createdByName;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 是否为当前有效策略
     */
    private Boolean isCurrentlyValid;

    /**
     * 策略描述信息
     */
    private String description;
}
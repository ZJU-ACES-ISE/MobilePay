package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DiscountStrategySearchDto 是折扣策略搜索条件数据传输对象，用于封装折扣策略查询的搜索条件
 *
 */
@Data
public class DiscountStrategySearchDto {

    /**
     * 搜索关键字（策略名称或策略编码）
     */
    @JsonProperty("keyword")
    private String keyword;

    /**
     * 策略状态：ACTIVE, INACTIVE, EXPIRED
     */
    @JsonProperty("status")
    private String status;

    /**
     * 策略类型：TRAVEL, PAYMENT, NEW_USER, HOLIDAY
     */
    @JsonProperty("strategy_type")
    private String strategyType;

    /**
     * 折扣类型：PERCENTAGE, FIXED_AMOUNT, LADDER
     */
    @JsonProperty("discount_type")
    private String discountType;

    /**
     * 目标用户类型：ALL, NEW, VIP, NORMAL
     */
    @JsonProperty("target_user_type")
    private String targetUserType;

    /**
     * 目标城市
     */
    @JsonProperty("target_city")
    private String targetCity;

    /**
     * 创建时间开始
     */
    @JsonProperty("start_time")
    private LocalDateTime startTime;

    /**
     * 创建时间结束
     */
    @JsonProperty("end_time")
    private LocalDateTime endTime;

    /**
     * 策略生效时间开始
     */
    @JsonProperty("valid_start_time")
    private LocalDateTime validStartTime;

    /**
     * 策略生效时间结束
     */
    @JsonProperty("valid_end_time")
    private LocalDateTime validEndTime;

    /**
     * 创建人ID
     */
    @JsonProperty("created_by")
    private Long createdBy;

    /**
     * 最小折扣金额
     */
    @JsonProperty("min_discount_amount")
    private BigDecimal minDiscountAmount;

    /**
     * 最大折扣金额
     */
    @JsonProperty("max_discount_amount")
    private BigDecimal maxDiscountAmount;

    /**
     * 是否可叠加使用
     */
    @JsonProperty("stackable")
    private Boolean stackable;

    /**
     * 是否显示过期策略
     */
    @JsonProperty("include_expired")
    private Boolean includeExpired = false;

    /**
     * 排序字段
     */
    @JsonProperty("order_by")
    private String orderBy;

    /**
     * 排序方向：ASC, DESC
     */
    @JsonProperty("order_direction")
    private String orderDirection;
}
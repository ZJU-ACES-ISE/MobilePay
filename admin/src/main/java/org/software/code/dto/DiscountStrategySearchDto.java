package org.software.code.dto;

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
    private String keyword;

    /**
     * 策略状态：ACTIVE, INACTIVE, EXPIRED
     */
    private String status;

    /**
     * 策略类型：TRAVEL, PAYMENT, NEW_USER, HOLIDAY
     */
    private String strategyType;

    /**
     * 折扣类型：PERCENTAGE, FIXED_AMOUNT, LADDER
     */
    private String discountType;

    /**
     * 目标用户类型：ALL, NEW, VIP, NORMAL
     */
    private String targetUserType;

    /**
     * 目标城市
     */
    private String targetCity;

    /**
     * 创建时间开始
     */
    private LocalDateTime startTime;

    /**
     * 创建时间结束
     */
    private LocalDateTime endTime;

    /**
     * 策略生效时间开始
     */
    private LocalDateTime validStartTime;

    /**
     * 策略生效时间结束
     */
    private LocalDateTime validEndTime;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 最小折扣金额
     */
    private BigDecimal minDiscountAmount;

    /**
     * 最大折扣金额
     */
    private BigDecimal maxDiscountAmount;

    /**
     * 是否可叠加使用
     */
    private Boolean stackable;

    /**
     * 是否显示过期策略
     */
    private Boolean includeExpired = false;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向：ASC, DESC
     */
    private String orderDirection;
}
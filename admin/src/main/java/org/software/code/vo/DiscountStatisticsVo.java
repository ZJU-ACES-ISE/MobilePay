package org.software.code.vo;

import lombok.Data;

/**
 * 折扣策略统计数据VO
 */
@Data
public class DiscountStatisticsVo {

    private Long totalStrategies;

    private Long activeStrategies;

    private Long inactiveStrategies;

    private Long totalUsageCount;

    private Double totalDiscountAmount;

}
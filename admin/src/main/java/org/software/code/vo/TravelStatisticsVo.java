package org.software.code.vo;

import lombok.Data;

/**
 * 出行统计数据VO
 */
@Data
public class TravelStatisticsVo {

    private Long totalTravelRecords;

    private Long totalScanRecords;

    private Long successfulScans;

    private Long failedScans;

    private Double totalAmount;

}
package org.software.code.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SiteSearchDto 是站点搜索条件数据传输对象，用于封装站点查询的搜索条件
 *
 */
@Data
public class SiteSearchDto {

    /**
     * 搜索关键字（站点名称或站点编码）
     */
    private String keyword;

    /**
     * 站点状态：ACTIVE, INACTIVE, MAINTENANCE
     */
    private String status;

    /**
     * 站点类型：MAIN, BRANCH
     */
    private String siteType;

    /**
     * 城市
     */
    private String city;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 交通类型：SUBWAY, BUS
     */
    private String type;

    /**
     * 创建时间开始
     */
    private LocalDateTime startTime;

    /**
     * 创建时间结束
     */
    private LocalDateTime endTime;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向：ASC, DESC
     */
    private String orderDirection;

    /**
     * 经度范围查询 - 最小经度
     */
    private BigDecimal minLongitude;

    /**
     * 经度范围查询 - 最大经度
     */
    private BigDecimal maxLongitude;

    /**
     * 纬度范围查询 - 最小纬度
     */
    private BigDecimal minLatitude;

    /**
     * 纬度范围查询 - 最大纬度
     */
    private BigDecimal maxLatitude;
}
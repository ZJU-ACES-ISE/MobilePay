package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("keyword")
    private String keyword;

    /**
     * 站点状态：ACTIVE, INACTIVE, MAINTENANCE
     */
    @JsonProperty("status")
    private String status;

    /**
     * 站点类型：MAIN, BRANCH
     */
    @JsonProperty("site_type")
    private String siteType;

    /**
     * 城市
     */
    @JsonProperty("city")
    private String city;

    /**
     * 线路名称
     */
    @JsonProperty("line_name")
    private String lineName;

    /**
     * 交通类型：SUBWAY, BUS
     */
    @JsonProperty("type")
    private String type;

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
     * 排序字段
     */
    @JsonProperty("order_by")
    private String orderBy;

    /**
     * 排序方向：ASC, DESC
     */
    @JsonProperty("order_direction")
    private String orderDirection;

    /**
     * 经度范围查询 - 最小经度
     */
    @JsonProperty("min_longitude")
    private BigDecimal minLongitude;

    /**
     * 经度范围查询 - 最大经度
     */
    @JsonProperty("max_longitude")
    private BigDecimal maxLongitude;

    /**
     * 纬度范围查询 - 最小纬度
     */
    @JsonProperty("min_latitude")
    private BigDecimal minLatitude;

    /**
     * 纬度范围查询 - 最大纬度
     */
    @JsonProperty("max_latitude")
    private BigDecimal maxLatitude;
}
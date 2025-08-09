package org.software.code.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DeviceSearchDto 是设备搜索条件数据传输对象，用于封装设备查询的搜索条件
 *
 */
@Data
public class DeviceSearchDto {

    /**
     * 搜索关键字（设备名称或设备编码）
     */
    private String keyword;

    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * 设备状态：ONLINE, OFFLINE, MAINTENANCE, FAULT
     */
    private String status;

    /**
     * 设备类型：ENTRY, EXIT, BOTH
     */
    private String deviceType;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 创建时间开始
     */
    private LocalDateTime startTime;

    /**
     * 创建时间结束
     */
    private LocalDateTime endTime;

    /**
     * 最后心跳时间开始
     */
    private LocalDateTime heartbeatStartTime;

    /**
     * 最后心跳时间结束
     */
    private LocalDateTime heartbeatEndTime;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向：ASC, DESC
     */
    private String orderDirection;

    /**
     * 是否在线（基于心跳时间）
     */
    private Boolean isOnline;

    /**
     * 心跳超时分钟数
     */
    private Integer heartbeatTimeoutMinutes = 5;

    /**
     * 交通类型：SUBWAY, BUS
     */
    private String type;
}
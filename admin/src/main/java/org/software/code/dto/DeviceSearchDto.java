package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("keyword")
    private String keyword;

    /**
     * 站点ID
     */
    @JsonProperty("site_id")
    private Long siteId;

    /**
     * 设备状态：ONLINE, OFFLINE, MAINTENANCE, FAULT
     */
    @JsonProperty("status")
    private String status;

    /**
     * 设备类型：ENTRY, EXIT, BOTH
     */
    @JsonProperty("device_type")
    private String deviceType;

    /**
     * 固件版本
     */
    @JsonProperty("firmware_version")
    private String firmwareVersion;

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
     * 最后心跳时间开始
     */
    @JsonProperty("heartbeat_start_time")
    private LocalDateTime heartbeatStartTime;

    /**
     * 最后心跳时间结束
     */
    @JsonProperty("heartbeat_end_time")
    private LocalDateTime heartbeatEndTime;

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
     * 是否在线（基于心跳时间）
     */
    @JsonProperty("is_online")
    private Boolean isOnline;

    /**
     * 心跳超时分钟数
     */
    @JsonProperty("heartbeat_timeout_minutes")
    private Integer heartbeatTimeoutMinutes = 5;
}
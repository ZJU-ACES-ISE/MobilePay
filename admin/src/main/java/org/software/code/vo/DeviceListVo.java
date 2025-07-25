package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DeviceListVo 是设备列表视图对象，用于封装设备列表信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceListVo {

    /**
     * 设备ID
     */
    @JsonProperty("device_id")
    private Long deviceId;

    /**
     * 设备编码
     */
    @JsonProperty("device_code")
    private String deviceCode;

    /**
     * 设备名称
     */
    @JsonProperty("device_name")
    private String deviceName;

    /**
     * 站点ID
     */
    @JsonProperty("site_id")
    private Long siteId;

    /**
     * 站点名称
     */
    @JsonProperty("site_name")
    private String siteName;

    /**
     * 站点编码
     */
    @JsonProperty("site_code")
    private String siteCode;

    /**
     * 设备类型
     */
    @JsonProperty("device_type")
    private String deviceType;

    /**
     * 设备类型名称
     */
    @JsonProperty("device_type_name")
    private String deviceTypeName;

    /**
     * 设备状态
     */
    @JsonProperty("status")
    private String status;

    /**
     * 设备状态名称
     */
    @JsonProperty("status_name")
    private String statusName;

    /**
     * 最后心跳时间
     */
    @JsonProperty("last_heartbeat")
    private LocalDateTime lastHeartbeat;

    /**
     * 固件版本
     */
    @JsonProperty("firmware_version")
    private String firmwareVersion;

    /**
     * IP地址
     */
    @JsonProperty("ip_address")
    private String ipAddress;

    /**
     * 设备位置描述
     */
    @JsonProperty("location")
    private String location;

    /**
     * 是否在线
     */
    @JsonProperty("is_online")
    private Boolean isOnline;

    /**
     * 创建时间
     */
    @JsonProperty("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @JsonProperty("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 获取设备类型名称
     */
    public String getDeviceTypeName() {
        if ("ENTRY".equals(this.deviceType)) {
            return "进站闸机";
        } else if ("EXIT".equals(this.deviceType)) {
            return "出站闸机";
        } else if ("BOTH".equals(this.deviceType)) {
            return "双向闸机";
        }
        return "未知类型";
    }

    /**
     * 获取设备状态名称
     */
    public String getStatusName() {
        if ("ONLINE".equals(this.status)) {
            return "在线";
        } else if ("OFFLINE".equals(this.status)) {
            return "离线";
        } else if ("MAINTENANCE".equals(this.status)) {
            return "维护中";
        } else if ("FAULT".equals(this.status)) {
            return "故障";
        }
        return "未知状态";
    }
}
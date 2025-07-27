package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DeviceDetailVo 是设备详情视图对象，用于封装设备详细信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDetailVo {

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
     * 站点信息
     */
    @JsonProperty("site_info")
    private SiteInfo siteInfo;

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
     * 设备描述
     */
    @JsonProperty("description")
    private String description;

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
     * MAC地址
     */
    @JsonProperty("mac_address")
    private String macAddress;

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
     * 创建人ID
     */
    @JsonProperty("created_by")
    private Long createdBy;

    /**
     * 更新人ID
     */
    @JsonProperty("updated_by")
    private Long updatedBy;

    /**
     * 创建人姓名
     */
    @JsonProperty("created_by_name")
    private String createdByName;

    /**
     * 更新人姓名
     */
    @JsonProperty("updated_by_name")
    private String updatedByName;

    /**
     * 设备状态历史记录
     */
    @JsonProperty("status_history")
    private List<DeviceStatusHistory> statusHistory;

    /**
     * 站点信息
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SiteInfo {
        /**
         * 站点ID
         */
        @JsonProperty("site_id")
        private Long siteId;

        /**
         * 站点编码
         */
        @JsonProperty("site_code")
        private String siteCode;

        /**
         * 站点名称
         */
        @JsonProperty("site_name")
        private String siteName;

        /**
         * 站点地址
         */
        @JsonProperty("site_address")
        private String siteAddress;

        /**
         * 站点状态
         */
        @JsonProperty("status")
        private String status;

        /**
         * 站点状态名称
         */
        @JsonProperty("status_name")
        private String statusName;

        /**
         * 获取站点状态名称
         */
        public String getStatusName() {
            if ("ACTIVE".equals(this.status)) {
                return "正常运营";
            } else if ("INACTIVE".equals(this.status)) {
                return "停运";
            } else if ("MAINTENANCE".equals(this.status)) {
                return "维护中";
            }
            return "未知状态";
        }
    }

    /**
     * 设备状态历史记录
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceStatusHistory {
        /**
         * 记录ID
         */
        @JsonProperty("record_id")
        private Long recordId;

        /**
         * 旧状态
         */
        @JsonProperty("old_status")
        private String oldStatus;

        /**
         * 新状态
         */
        @JsonProperty("new_status")
        private String newStatus;

        /**
         * 变更原因
         */
        @JsonProperty("change_reason")
        private String changeReason;

        /**
         * 操作人
         */
        @JsonProperty("operator_name")
        private String operatorName;

        /**
         * 变更时间
         */
        @JsonProperty("change_time")
        private LocalDateTime changeTime;
    }

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
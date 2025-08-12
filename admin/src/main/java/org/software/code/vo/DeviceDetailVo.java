package org.software.code.vo;

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
    private Long deviceId;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * 站点信息
     */
    private SiteInfo siteInfo;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 设备状态名称
     */
    private String statusName;

    /**
     * 交通类型
     */
    private String type;

    /**
     * 交通类型名称
     */
    private String typeName;

    /**
     * 设备描述
     */
    private String description;

    /**
     * 最后心跳时间
     */
    private LocalDateTime lastHeartbeat;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * 设备位置描述
     */
    private String location;

    /**
     * 是否在线
     */
    private Boolean isOnline;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 更新人ID
     */
    private Long updatedBy;

    /**
     * 创建人姓名
     */
    private String createdByName;

    /**
     * 更新人姓名
     */
    private String updatedByName;

    /**
     * 设备状态历史记录
     */
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
        private Long siteId;

        /**
         * 站点编码
         */
        private String siteCode;

        /**
         * 站点名称
         */
        private String siteName;

        /**
         * 站点地址
         */
        private String siteAddress;

        /**
         * 站点状态
         */
        private String status;

        /**
         * 站点状态名称
         */
        private String statusName;

        /**
         * 交通类型
         */
        private String type;

        /**
         * 交通类型名称
         */
        private String typeName;

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

        /**
         * 获取交通类型名称
         */
        public String getTypeName() {
            if ("SUBWAY".equals(this.type)) {
                return "地铁";
            } else if ("BUS".equals(this.type)) {
                return "公交";
            }
            return "未知类型";
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
        private Long recordId;

        /**
         * 旧状态
         */
        private String oldStatus;

        /**
         * 新状态
         */
        private String newStatus;

        /**
         * 变更原因
         */
        private String changeReason;

        /**
         * 操作人
         */
        private String operatorName;

        /**
         * 变更时间
         */
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

    /**
     * 获取交通类型名称
     */
    public String getTypeName() {
        if ("SUBWAY".equals(this.type)) {
            return "地铁";
        } else if ("BUS".equals(this.type)) {
            return "公交";
        }
        return "未知类型";
    }
}
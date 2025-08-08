package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SiteDetailVo 是站点详情视图对象，用于封装站点详细信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteDetailVo {

    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * 站点名称
     */
    private String siteName;

    /**
     * 站点编码
     */
    private String siteCode;

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
     * 城市
     */
    private String city;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 营业开始时间
     */
    private String businessStartTime;

    /**
     * 营业结束时间
     */
    private String businessEndTime;

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
     * 关联设备列表
     */
    private List<DeviceSimpleVo> devices;

    /**
     * 客流量统计信息
     */
    private PassengerFlowStats passengerFlowStats;

    /**
     * 设备简单视图对象
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceSimpleVo {
        /**
         * 设备ID
         */
        private Long deviceId;

        /**
         * 设备编号
         */
        private String deviceCode;

        /**
         * 设备名称
         */
        private String deviceName;

        /**
         * 设备状态
         */
        private String status;

        /**
         * 设备状态名称
         */
        private String statusName;

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

    /**
     * 客流量统计信息
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PassengerFlowStats {
        /**
         * 今日进站人数
         */
        private Long todayEntryCount;

        /**
         * 今日出站人数
         */
        private Long todayExitCount;

        /**
         * 今日总客流量
         */
        private Long todayTotalFlow;

        /**
         * 日均客流量（当前月份）
         */
        private Double averageDailyFlow;
    }

    /**
     * 获取状态名称
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
package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
     * 站点地址
     */
    @JsonProperty("site_address")
    private String siteAddress;

    /**
     * 联系人
     */
    @JsonProperty("contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    @JsonProperty("contact_phone")
    private String contactPhone;

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
     * 站点类型
     */
    @JsonProperty("site_type")
    private String siteType;

    /**
     * 站点类型名称
     */
    @JsonProperty("site_type_name")
    private String siteTypeName;

    /**
     * 交通类型
     */
    @JsonProperty("type")
    private String type;

    /**
     * 交通类型名称
     */
    @JsonProperty("type_name")
    private String typeName;

    /**
     * 站点描述
     */
    @JsonProperty("description")
    private String description;

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
     * 经度
     */
    @JsonProperty("longitude")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @JsonProperty("latitude")
    private BigDecimal latitude;

    /**
     * 营业开始时间
     */
    @JsonProperty("business_start_time")
    private String businessStartTime;

    /**
     * 营业结束时间
     */
    @JsonProperty("business_end_time")
    private String businessEndTime;

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
     * 关联设备列表
     */
    @JsonProperty("devices")
    private List<DeviceSimpleVo> devices;

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
        @JsonProperty("device_id")
        private Long deviceId;

        /**
         * 设备编号
         */
        @JsonProperty("device_code")
        private String deviceCode;

        /**
         * 设备名称
         */
        @JsonProperty("device_name")
        private String deviceName;

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
     * 获取站点类型名称
     */
    public String getSiteTypeName() {
        if ("MAIN".equals(this.siteType)) {
            return "主站点";
        } else if ("BRANCH".equals(this.siteType)) {
            return "分站点";
        }
        return "未知类型";
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
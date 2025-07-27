package org.software.code.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SiteListVo 是站点列表视图对象，用于封装站点列表信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteListVo {

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
}
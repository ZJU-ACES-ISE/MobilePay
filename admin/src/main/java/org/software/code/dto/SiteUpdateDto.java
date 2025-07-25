package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * SiteUpdateDto 是站点更新请求数据传输对象，用于封装更新站点的请求信息
 *
 */
@Data
public class SiteUpdateDto {

    /**
     * 站点名称
     */
    @Size(max = 100, message = "站点名称长度不能超过100字符")
    @JsonProperty("site_name")
    private String siteName;

    /**
     * 站点地址
     */
    @Size(max = 200, message = "站点地址长度不能超过200字符")
    @JsonProperty("site_address")
    private String siteAddress;

    /**
     * 联系人
     */
    @Size(max = 50, message = "联系人姓名长度不能超过50字符")
    @JsonProperty("contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    @JsonProperty("contact_phone")
    private String contactPhone;

    /**
     * 站点状态：ACTIVE, INACTIVE, MAINTENANCE
     */
    @Pattern(regexp = "^(ACTIVE|INACTIVE|MAINTENANCE)$", message = "站点状态只能是ACTIVE、INACTIVE或MAINTENANCE")
    @JsonProperty("status")
    private String status;

    /**
     * 站点类型：MAIN, BRANCH
     */
    @Pattern(regexp = "^(MAIN|BRANCH)$", message = "站点类型只能是MAIN或BRANCH")
    @JsonProperty("site_type")
    private String siteType;

    /**
     * 站点描述
     */
    @Size(max = 500, message = "站点描述长度不能超过500字符")
    @JsonProperty("description")
    private String description;

    /**
     * 经度
     */
    @DecimalMin(value = "-180", message = "经度必须在-180到180之间")
    @DecimalMax(value = "180", message = "经度必须在-180到180之间")
    @JsonProperty("longitude")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @DecimalMin(value = "-90", message = "纬度必须在-90到90之间")
    @DecimalMax(value = "90", message = "纬度必须在-90到90之间")
    @JsonProperty("latitude")
    private BigDecimal latitude;

    /**
     * 城市
     */
    @Size(max = 50, message = "城市名称长度不能超过50字符")
    @JsonProperty("city")
    private String city;

    /**
     * 线路名称
     */
    @Size(max = 50, message = "线路名称长度不能超过50字符")
    @JsonProperty("line_name")
    private String lineName;

    /**
     * 营业开始时间（HH:mm格式）
     */
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "营业开始时间格式不正确，应为HH:mm")
    @JsonProperty("business_start_time")
    private String businessStartTime;

    /**
     * 营业结束时间（HH:mm格式）
     */
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "营业结束时间格式不正确，应为HH:mm")
    @JsonProperty("business_end_time")
    private String businessEndTime;
}
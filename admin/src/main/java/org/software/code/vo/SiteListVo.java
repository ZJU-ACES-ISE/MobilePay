package org.software.code.vo;

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
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 站点状态
     */
    private String status;

    /**
     * 站点状态名称
     */
    private String statusName;

    /**
     * 站点类型
     */
    private String siteType;

    /**
     * 站点类型名称
     */
    private String siteTypeName;

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
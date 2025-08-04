package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站点视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVo {
    
    /**
     * 站点ID
     */
    private Long id;
    
    /**
     * 站点编号
     */
    private String siteCode;
    
    /**
     * 站点名称
     */
    private String siteName;
    
    /**
     * 所在城市
     */
    private String city;
    
    /**
     * 城市编码
     */
    private String cityCode;
    
    /**
     * 所属线路
     */
    private String line;
    
    /**
     * 经度
     */
    private Double longitude;
    
    /**
     * 纬度
     */
    private Double latitude;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 站点类型（SUBWAY地铁站，BUS公交站）
     */
    private String type;
} 
package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 站点实体类
 */
@Data
@TableName("site")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
    @TableField("line_name")
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

    /**
     * 状态（ACTIVE正常，INACTIVE停用）
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
} 
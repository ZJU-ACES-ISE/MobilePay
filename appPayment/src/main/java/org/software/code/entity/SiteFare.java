package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 站点间费用实体类
 */
@Data
@TableName("site_fare")
public class SiteFare implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 起始站点ID
     */
    private Long fromSiteId;

    /**
     * 终点站点ID
     */
    private Long toSiteId;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 交通类型（SUBWAY地铁，BUS公交）
     */
    private String transitType;

    /**
     * 基础票价
     */
    private BigDecimal baseFare;

    /**
     * 距离（公里）
     */
    private Double distance;

    /**
     * 站点数量
     */
    private Integer stationCount;

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
package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通行码实体类
 */
@Data
@TableName("transit_pass")
public class TransitPass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 城市ID
     */
    private String cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 通行码链接
     */
    private String codeUrl;

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
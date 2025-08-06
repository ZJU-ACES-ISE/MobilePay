package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 闸机设备实体类
 */
@Data
@TableName("turnstile_device")
public class TurnstileDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 所属站点ID
     */
    private Long siteId;

    /**
     * 设备位置描述
     */
    private String location;

    /**
     * 设备类型（ENTRY入站，EXIT出站，DUAL双向）
     */
    private String type;

    /**
     * 状态（ACTIVE正常，INACTIVE停用，MAINTENANCE维护中）
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
package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出行记录实体类
 */
@Data
@TableName("transit_record")
public class TransitRecord implements Serializable {

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
     * 出行方式(如地铁/公交)
     */
    private String mode;

    /**
     * 入站站点ID
     */
    private Long entrySiteId;

    /**
     * 出站站点ID
     */
    private Long exitSiteId;

    /**
     * 入站设备ID
     */
    private Long entryDeviceId;

    /**
     * 出站设备ID
     */
    private Long exitDeviceId;

    /**
     * 入站时间
     */
    private LocalDateTime entryTime;

    /**
     * 出站时间
     */
    private LocalDateTime exitTime;

    /**
     * 费用
     */
    private BigDecimal amount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 实际扣费金额
     */
    private BigDecimal actualAmount;

    /**
     * 出站状态（0正常，1支付异常，2出行异常）
     */
    private Integer status;

    /**
     * 异常原因（status为1或2时记录）
     */
    private String reason;

    /**
     * 交易记录编号（正常出站时记录）
     */
    private String transactionId;

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
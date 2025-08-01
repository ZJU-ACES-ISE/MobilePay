package org.software.code.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户出行记录VO
 * 
 */
@Data
@Builder
public class TransitRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 出行方式
     */
    private String mode;

    /**
     * 城市
     */
    private String city;

    /**
     * 进站站点名称
     */
    private String entrySiteName;

    /**
     * 出站站点名称
     */
    private String exitSiteName;

    /**
     * 进站设备名称
     */
    private String entryDeviceName;

    /**
     * 出站设备名称
     */
    private String exitDeviceName;

    /**
     * 进站时间
     */
    private LocalDateTime entryTime;

    /**
     * 出站时间
     */
    private LocalDateTime exitTime;

    /**
     * 原始费用
     */
    private BigDecimal amount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;

    /**
     * 出行状态
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 原因
     */
    private String reason;

    /**
     * 交易ID
     */
    private String transactionId;

    /**
     * 出行时长（分钟）
     */
    private Long durationMinutes;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
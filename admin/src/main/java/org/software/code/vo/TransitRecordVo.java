package org.software.code.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户出行记录VO（管理后台使用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
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
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TransactionVo 是交易记录视图对象，用于封装用户交易记录信息
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionVo {

    /**
     * 交易记录ID
     */
    private Long transactionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 交易类型：TRAVEL（出行）、PAYMENT（支付）、RECHARGE（充值）、WITHDRAW（提现）
     */
    private String transactionType;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 交易状态：IN_PROGRESS（进行中）、COMPLETED（已完成）、CANCELLED（已取消）、FAILED（失败）
     */
    private String status;

    /**
     * 支付方式：BALANCE（余额）、CARD（银行卡）
     */
    private String paymentMethod;

    /**
     * 入站站点名称
     */
    private String entryStationName;

    /**
     * 出站站点名称
     */
    private String exitStationName;

    /**
     * 入站时间
     */
    private LocalDateTime entryTime;

    /**
     * 出站时间
     */
    private LocalDateTime exitTime;

    /**
     * 交易描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
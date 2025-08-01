package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收款交易记录表
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 * @since 2025-07-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("receipt_transaction")
public class ReceiptTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("transaction_id")
    private String transactionId;

    @TableField("payer_id")
    private String payerId;

    @TableField("receiver_id")
    private String receiverId;

    @TableField("receiver_name")
    private String receiverName;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("receipt_code_id")
    private String receiptCodeId;

    @TableField("timestamp")
    private LocalDateTime timestamp;

    @TableField("create_time")
    private LocalDateTime createTime;


}

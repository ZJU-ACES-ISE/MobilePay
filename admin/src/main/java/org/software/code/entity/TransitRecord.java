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
 * 
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 * @since 2025-07-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("transit_record")
public class TransitRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("mode")
    private String mode;

    @TableField("entry_site_id")
    private Long entrySiteId;

    @TableField("exit_site_id")
    private Long exitSiteId;

    @TableField("entry_device_id")
    private Long entryDeviceId;

    @TableField("exit_device_id")
    private Long exitDeviceId;

    @TableField("entry_time")
    private LocalDateTime entryTime;

    @TableField("exit_time")
    private LocalDateTime exitTime;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @TableField("actual_amount")
    private BigDecimal actualAmount;

    @TableField("status")
    private Integer status;

    @TableField("reason")
    private String reason;

    @TableField("transaction_id")
    private String transactionId;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;


}

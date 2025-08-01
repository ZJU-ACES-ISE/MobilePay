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
@TableName("transfer_record")
public class TransferRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("transfer_number")
    private String transferNumber;

    @TableField("user_id")
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("type")
    private Integer type;

    @TableField("bank_card_id")
    private Long bankCardId;

    @TableField("target_id")
    private Long targetId;

    @TableField("target_type")
    private Integer targetType;

    @TableField("target_name")
    private String targetName;

    @TableField("biz_category")
    private Integer bizCategory;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @TableField("actual_amount")
    private BigDecimal actualAmount;

    @TableField("complete_time")
    private LocalDateTime completeTime;

    @TableField("remark")
    private String remark;


}

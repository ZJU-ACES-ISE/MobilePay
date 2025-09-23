package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额实体类
 *
 * 对应表：user_balance
 */
@Data
@TableName("user_balance")
@Schema(name = "UserBalance", description = "用户余额表实体")
public class UserBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID（唯一）")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "用户当前余额")
    private BigDecimal balance;

    @Schema(description = "最后更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
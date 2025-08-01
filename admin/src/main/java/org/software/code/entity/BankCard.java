package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户银行卡表（卡包）
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 * @since 2025-07-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bank_card")
public class BankCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("card_name")
    private String cardName;

    @TableField("card_phone")
    private String cardPhone;

    @TableField("bank_name")
    private String bankName;

    @TableField("card_number")
    private String cardNumber;

    @TableField("type")
    private Integer type;

    @TableField("status")
    private Integer status;

    @TableField("bind_time")
    private LocalDateTime bindTime;

    @TableField("last_four_digits")
    private String lastFourDigits;


}

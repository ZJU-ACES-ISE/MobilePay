package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * BankCard 实体类，用于表示用户银行卡信息（卡包）。
 * 包含用户绑定的银行卡的基本信息、类型、状态以及卡号尾号等。
 * 自动生成卡号尾号用于脱敏展示。
 *
 * 对应数据库表：bank_card
 *
 * @author
 */
@Data
@TableName("bank_card")
public class BankCard implements Serializable {

    /**
     * 银行卡记录ID，主键
     */
    @TableId
    private Long id;

    /**
     * 所属用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 持卡人姓名（实名信息）
     */
    @NotNull
    private String cardName;

    /**
     * 银行预留手机号
     */
    @NotNull
    private String cardPhone;

    /**
     * 银行名称（如“招商银行”）
     */
    @NotNull
    private String bankName;

    /**
     * 银行卡号（建议脱敏展示，仅用于系统识别）
     */
    @NotNull
    private String cardNumber;

    /**
     * 银行卡类型：1=储蓄卡，2=信用卡
     */
    private Integer type;

    /**
     * 是否为默认卡：1=默认，2=非默认
     */
    private Integer status;

    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;

    /**
     * 卡号尾号，自动生成，用于展示
     * 此字段为数据库计算列，不能手动插入或更新
     */
    @TableField(exist = false)
    private String lastFourDigits;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}



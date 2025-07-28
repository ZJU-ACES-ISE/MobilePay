package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BankCard 实体类，用于表示用户银行卡信息（卡包）。
 * 对应数据库表：bank_card
 */
@Data
@TableName("bank_card")
@Schema(name = "BankCard", description = "用户银行卡实体，包含持卡人信息、卡类型、绑定时间等")
public class BankCard implements Serializable {

    @TableId
    @Schema(description = "银行卡记录ID，主键")
    private Long id;

    @NotNull
    @Schema(description = "所属用户ID")
    private Long userId;

    @NotNull
    @Schema(description = "持卡人姓名（实名信息）")
    private String cardName;

    @NotNull
    @Schema(description = "银行预留手机号")
    private String cardPhone;

    @NotNull
    @Schema(description = "银行名称（如“招商银行”）")
    private String bankName;

    @NotNull
    @Schema(description = "银行卡号（系统识别使用，建议脱敏展示）")
    private String cardNumber;

    @Schema(description = "银行卡类型：1=储蓄卡，2=信用卡")
    private Integer type;

    @Schema(description = "是否为默认卡：1=默认，2=非默认")
    private Integer status;

    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;

    @Schema(description = "卡号尾号，自动从 cardNumber 中生成，仅用于展示")
    private String lastFourDigits;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

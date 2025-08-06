package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * AddBankCardDto 是添加银行卡的请求数据传输对象
 * 包含持卡人姓名、手机号、银行卡号、银行名称及卡类型等信息
 *
 * @author
 */
@Data
public class AddBankCardDto {

    /**
     * 持卡人姓名（实名信息）
     */
    @Schema(description = "持卡人姓名", example = "张三")
    @NotBlank(message = "持卡人姓名不能为空")
    private String cardName;

    /**
     * 银行预留手机号
     */
    @Schema(description = "银行卡预留手机号", example = "13800001111")
    @NotBlank(message = "银行预留手机号不能为空")
    private String cardPhone;

    /**
     * 银行名称（如“招商银行”）
     */
    @Schema(description = "银行名称", example = "招商银行")
    @NotBlank(message = "银行名称不能为空")
    private String bankName;

    /**
     * 银行卡号
     */
    @Schema(description = "银行卡号", example = "6225888888888888")
    @NotBlank(message = "银行卡号不能为空")
    private String cardNumber;

    /**
     * 银行卡类型：1=储蓄卡，2=信用卡
     */
    @Schema(description = "银行卡类型 1=储蓄卡，2=信用卡", example = "1")
    @NotNull(message = "银行卡类型不能为空")
    private Integer type;
}
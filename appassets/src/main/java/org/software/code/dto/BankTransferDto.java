package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 用户转入充值请求体
 */
@Data
@Schema(description = "充值/提现请求对象")
public class BankTransferDto {

    @Schema(description = "银行卡ID", example = "10001")
    @NotNull(message = "银行卡ID不能为空")
    private Long bankCardId;

    @Schema(description = "银行名称", example = "招商银行")
    @NotBlank(message = "银行名称不能为空")
    private String bankName;

    @Schema(description = "金额", example = "500")
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

    @Schema(description = "支付密码（6位数字）", example = "123456")
    @Pattern(regexp = "^\\d{6}$", message = "支付密码必须是6位数字")
    private String payPassword;
}

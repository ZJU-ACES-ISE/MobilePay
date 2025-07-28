package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VerifyBankCardCodeDto {

    /**
     * 手机号
     */
    @Schema(description = "银行卡预留手机号", example = "13800001111")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 验证码（6位）
     */
    @Schema(description = "六位验证码", example = "123456")
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}

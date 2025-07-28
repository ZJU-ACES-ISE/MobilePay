package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendCodeRequestDto {

    @Schema(description = "手机号", example = "13812345678")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "银行卡号", example = "6222021234567890123")
    @NotBlank(message = "银行卡号不能为空")
    private String cardNumber;
}

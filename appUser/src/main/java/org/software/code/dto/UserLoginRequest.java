package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户登录请求DTO
 */
@Data
@Schema(description = "用户登录请求")
public class UserLoginRequest {
    
    /**
     * 手机号
     */
    @Schema(description = "手机号", required = true, example = "13812345678")
    private String phone;
    
    /**
     * 登录类型：password-密码登录, verifyCode-验证码登录
     */
    @Schema(description = "登录类型", required = true, example = "password", allowableValues = {"password", "verifyCode"})
    private String loginType;
    
    /**
     * 登录密码（密码登录时必填）
     */
    @Schema(description = "登录密码", example = "Abcd1234")
    private String loginPassword;
    
    /**
     * 验证码（验证码登录时必填）
     */
    @Schema(description = "验证码", example = "123456")
    private String verifyCode;
}
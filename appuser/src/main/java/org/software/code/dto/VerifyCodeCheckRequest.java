package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 验证码校验请求DTO
 */
@Data
@Schema(description = "验证码校验请求")
public class VerifyCodeCheckRequest {
    
    /**
     * 手机号
     */
    @Schema(description = "手机号", required = true, example = "13812345678")
    private String phone;
    
    /**
     * 验证码
     */
    @Schema(description = "验证码", required = true, example = "123456")
    private String verifyCode;
    
    /**
     * 场景
     */
    @Schema(description = "场景", required = true, example = "register")
    private String scene;
} 
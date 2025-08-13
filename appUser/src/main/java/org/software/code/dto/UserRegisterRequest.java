package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户注册请求DTO
 */
@Data
@Schema(description = "用户注册请求")
public class UserRegisterRequest {
    
    /**
     * 手机号
     */
    @Schema(description = "电话", required = true, example = "13812345678")
    private String phone;
    
    /**
     * 登录密码
     */
    @Schema(description = "登录密码", required = true, example = "Abcd1234")
    private String loginPassword;
    
    /**
     * 支付密码
     */
    @Schema(description = "支付密码", required = true, example = "654321")
    private String payPassword;
} 
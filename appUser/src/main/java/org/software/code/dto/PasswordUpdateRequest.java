package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 密码更新请求DTO，适用于登录密码和支付密码的更新
 */
@Data
@Schema(description = "密码更新请求")
public class PasswordUpdateRequest {
    
    /**
     * 旧密码
     */
    @Schema(description = "旧密码", required = true, example = "123456")
    private String oldPassword;
    
    /**
     * 新密码
     */
    @Schema(description = "新密码", required = true, example = "654321")
    private String newPassword;
} 
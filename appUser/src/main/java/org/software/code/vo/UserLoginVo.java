package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录响应")
public class UserLoginVo {
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", required = true, example = "u123456")
    private String userId;
    
    /**
     * 认证Token
     */
    @Schema(description = "认证Token", required = true, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    /**
     * 手机号
     */
    @Schema(description = "手机号", required = true, example = "13812345678")
    private String phone;
}
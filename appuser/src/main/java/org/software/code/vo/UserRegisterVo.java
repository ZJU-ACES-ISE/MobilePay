package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册返回VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户注册返回数据")
public class UserRegisterVo {
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", required = true)
    private String userId;
    
    /**
     * 令牌
     */
    @Schema(description = "token", required = true)
    private String token;
    
    /**
     * 手机号
     */
    @Schema(description = "手机号", required = true)
    private String phone;
} 
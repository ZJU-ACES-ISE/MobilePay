package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码视图对象，用于返回验证码相关信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeVo {
    /**
     * 验证码
     */
    private Integer code;
    
    /**
     * 过期时间(秒)
     */
    private Integer expireIn;
} 
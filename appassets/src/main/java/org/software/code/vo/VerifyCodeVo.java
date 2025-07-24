package org.software.code.vo;

import lombok.Builder;
import lombok.Data;

/**
 * VerifyCodeVo 是发送验证码返回结果的视图对象
 * 包含验证码内容和过期时间，用于模拟发送服务
 *
 * @author
 */
@Data
@Builder
public class VerifyCodeVo {

    /**
     * 验证码（模拟）
     */
    private String code;

    /**
     * 过期时间(秒)
     */
    private Long expireTime;
}
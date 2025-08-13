package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * VerifyCodeVo 是发送验证码返回结果的视图对象
 * 包含验证码内容和过期时间，用于模拟发送服务
 */
@Data
@Builder
@Schema(name = "VerifyCodeVo", description = "验证码返回视图对象，包含验证码内容和过期时间")
public class VerifyCodeVo {

    @Schema(description = "验证码（模拟）", example = "123456")
    private String code;

    @Schema(description = "验证码过期时间（秒）", example = "300")
    private Long expireTime;
}

package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.vo.VerifyCodeVo;

/**
 * 验证码服务接口
 */
public interface VerifyCodeService {

    /**
     * 发送验证码
     * @param phone 手机号
     * @param scene 场景，如 "register"(注册)、"login"(登录) 等
     * @return 结果，包含验证码信息或错误信息
     */
    Result<VerifyCodeVo> sendVerifyCode(String phone, String scene);
    
    /**
     * 校验验证码
     * @param phone 手机号
     * @param verifyCode 验证码
     * @param scene 场景，如 "register"(注册)、"login"(登录) 等
     * @return 校验结果
     */
    Result<?> checkVerifyCode(String phone, String verifyCode, String scene);
} 
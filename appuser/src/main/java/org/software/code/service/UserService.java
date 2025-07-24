package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.vo.UserRegisterVo;
import org.software.code.vo.UserVo;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 查询个人信息
     * @param token 用户token
     * @return 用户个人信息
     */
    Result<UserVo> getUserProfile(String token);
    
    /**
     * 用户注册
     * @param phone 手机号
     * @param loginPassword 登录密码
     * @param payPassword 支付密码
     * @return 注册结果
     */
    Result<UserRegisterVo> register(String phone, String loginPassword, String payPassword);
} 
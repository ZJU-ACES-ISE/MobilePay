package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.PasswordUpdateRequest;
import org.software.code.dto.UserProfileUpdateRequest;
import org.software.code.vo.UserRegisterVo;
import org.software.code.vo.UserVo;
import org.software.code.vo.UserLoginVo;

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
    
    /**
     * 用户登录
     * @param phone 手机号
     * @param loginType 登录类型：password-密码登录, verifyCode-验证码登录
     * @param credential 凭证(密码或验证码)
     * @return 登录结果
     */
    Result<UserLoginVo> login(String phone, String loginType, String credential);
    
    /**
     * 更新用户个人信息
     * @param token 用户token
     * @param request 更新请求
     * @return 更新后的用户信息
     */
    Result<UserVo> updateUserProfile(String token, UserProfileUpdateRequest request);
    
    /**
     * 修改支付密码
     * @param token 用户token
     * @param request 支付密码更新请求
     * @return 修改结果
     */
    Result<?> updatePaymentPassword(String token, PasswordUpdateRequest request);
    
    /**
     * 更新登录密码
     * @param token JWT token
     * @param request 密码更新请求
     * @return 更新结果
     */
    Result<Void> updatePassword(String token, PasswordUpdateRequest request);

    /**
     * 退出登录
     * @param token JWT token
     * @return 退出结果
     */
    Result<Void> logout(String token);
} 
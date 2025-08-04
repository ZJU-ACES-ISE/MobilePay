package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.service.UserService;
import org.software.code.vo.VerifyCodeVo;
import org.software.code.vo.UserVo;
import org.software.code.dto.UserRegisterRequest;
import org.software.code.dto.UserLoginRequest;
import org.software.code.dto.UserProfileUpdateRequest;
import org.software.code.dto.PasswordUpdateRequest;
import org.software.code.vo.UserRegisterVo;
import org.software.code.vo.UserLoginVo;
import org.software.code.dto.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

@Tag(name = "用户相关接口", description = "用户登录、注册、验证码等操作")
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询个人信息
     * @param token 用户token
     * @return 个人信息
     */
    @Operation(summary = "查询个人信息", description = "无需传参，系统从 token 中识别当前用户")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "Token无效或已过期")
    })
    @GetMapping("/profile")
    public Result<UserVo> getUserProfile(
            @Parameter(description = "Bearer 类型 Token 认证", required = true) 
            @RequestHeader("Authorization") String token) {
        return userService.getUserProfile(token);
    }

    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册结果
     */
    @Operation(summary = "用户注册", description = "完成验证码校验后，用户提交注册信息，此处不再校验手机号是否存在，也不再传验证码字段")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "注册成功"),
        @ApiResponse(responseCode = "400", description = "注册失败")
    })
    @PostMapping("/register")
    public Result<UserRegisterVo> register(@RequestBody UserRegisterRequest request) {
        return userService.register(request.getPhone(), request.getLoginPassword(), request.getPayPassword());
    }
    
    /**
     * 用户登录
     * @param request 登录请求
     * @return 登录结果
     */
    @Operation(summary = "用户登录", description = "已注册用户通过手机号 + 验证码 或 手机号 + 密码登录，支持两种登录方式，通过 loginType 参数控制")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "登录成功"),
        @ApiResponse(responseCode = "400", description = "登录失败")
    })
    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestBody UserLoginRequest request) {
        String credential = "password".equals(request.getLoginType()) ? 
                request.getLoginPassword() : request.getVerifyCode();
        return userService.login(request.getPhone(), request.getLoginType(), credential);
    }
    
    /**
     * 退出登录
     * @param token 用户token
     * @return 退出结果
     */
    @Operation(summary = "退出登录", description = "用户退出登录，使当前token失效")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "退出成功"),
        @ApiResponse(responseCode = "401", description = "无效的登录状态")
    })
    @DeleteMapping("/logout")
    public Result<?> logout(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String token) {
        return userService.logout(token);
    }
    
    /**
     * 修改个人信息
     * @param token 用户token
     * @param request 修改请求
     * @return 修改结果
     */
    @Operation(summary = "修改个人信息", description = "任意字段可选，支持单字段或多个字段同时修改")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "修改成功"),
        @ApiResponse(responseCode = "400", description = "修改失败")
    })
    @PutMapping("/profile/update")
    public Result<UserVo> updateProfile(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileUpdateRequest request) {
        return userService.updateUserProfile(token, request);
    }
    
    /**
     * 修改支付密码
     * @param token 用户token
     * @param request 修改支付密码请求
     * @return 修改结果
     */
    @Operation(summary = "修改支付密码", description = "用于对于支付密码的修改")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "密码修改成功"),
        @ApiResponse(responseCode = "401", description = "原密码不正确")
    })
    @PutMapping("/payment-password/update")
    public Result<?> updatePaymentPassword(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordUpdateRequest request) {
        return userService.updatePaymentPassword(token, request);
    }
    
    /**
     * 修改登录密码
     * @param token 用户token
     * @param request 修改登录密码请求
     * @return 修改结果
     */
    @Operation(summary = "修改登录密码", description = "用于用户密码的修改")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "密码修改成功"),
        @ApiResponse(responseCode = "401", description = "原密码不正确")
    })
    @PutMapping("/password/update")
    public Result<?> updatePassword(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordUpdateRequest request) {
        return userService.updatePassword(token, request);
    }

    /**
     * 忘记密码（重置密码）
     * @param request 重置密码请求
     * @return 重置结果
     */
    @Operation(summary = "忘记密码", description = "使用手机号和验证码重置密码")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "密码重置成功"),
        @ApiResponse(responseCode = "400", description = "参数错误"),
        @ApiResponse(responseCode = "401", description = "验证码错误或已过期")
    })
    @PostMapping("/password/reset")
    public Result<?> resetPassword(@Validated @RequestBody ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }
}

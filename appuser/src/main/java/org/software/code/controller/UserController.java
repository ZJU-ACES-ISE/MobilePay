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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户相关接口", description = "用户登录、注册、验证码等操作")
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取验证码接口
     * @param phone 手机号
     * @param scene 场景
     * @param token 登录token
     * @return 统一返回结构，包含验证码信息
     */
    @Operation(summary = "获取验证码", description = "用户输入手机号，点击\"发送验证码\"时调用，场景可为 register、resetPassword 等")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "验证码发送成功"),
        @ApiResponse(responseCode = "409", description = "手机号已被注册，请更换手机号")
    })
    @GetMapping("/sendVerifyCode")
    public Result<VerifyCodeVo> sendVerifyCode(
            @Parameter(description = "手机号（11位）", required = true, example = "13812345678") 
            @RequestParam String phone,
            
            @Parameter(description = "验证码使用场景", required = true, example = "register") 
            @RequestParam String scene,
            
            @Parameter(description = "用户token", required = true) 
            @RequestHeader("Authorization") String token) {
        return userService.sendVerifyCode(phone, scene, token);
    }

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
}

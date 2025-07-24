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
import org.software.code.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}

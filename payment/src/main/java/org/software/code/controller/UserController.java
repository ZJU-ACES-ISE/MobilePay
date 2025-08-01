package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.service.UserService;
import org.software.code.vo.UserBalanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户相关接口", description = "用户信息、余额查询等操作")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户余额
     * @param authorization Bearer类型Token认证
     * @return 用户余额信息
     */
    @Operation(summary = "查询用户余额", description = "查询用户余额")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "Token无效或已过期"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/balance")
    public Result<UserBalanceVo> getUserBalance(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization) {
        return userService.getUserBalance(authorization);
    }
} 
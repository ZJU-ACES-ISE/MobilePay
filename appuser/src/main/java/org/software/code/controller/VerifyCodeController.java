package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.dto.VerifyCodeCheckRequest;
import org.software.code.service.VerifyCodeService;
import org.software.code.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "验证码相关接口", description = "验证码发送、校验等操作")
@Validated
@RestController
@RequestMapping("/user/verifyCode")
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 发送验证码接口
     * @param phone 手机号
     * @param scene 场景
     * @return 统一返回结构，包含验证码信息
     */
    @Operation(summary = "发送验证码", description = "用户输入手机号，点击发送验证码时调用，场景可为 register、resetPassword 等")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "验证码发送成功"),
        @ApiResponse(responseCode = "409", description = "手机号已被注册，请更换手机号")
    })
    @GetMapping("/send")
    public Result<VerifyCodeVo> sendVerifyCode(
            @Parameter(description = "手机号（11位）", required = true, example = "13812345678") 
            @RequestParam String phone,
            
            @Parameter(description = "验证码使用场景", required = true, example = "register") 
            @RequestParam String scene) {
        
        // 发送验证码，token验证由网关统一处理
        return verifyCodeService.sendVerifyCode(phone, scene);
    }
    
    /**
     * 校验验证码接口
     * @param request 验证码校验请求
     * @return 校验结果
     */
    @Operation(summary = "校验验证码", description = "用户输入验证码点击下一步时调用，成功后方可调用注册接口")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "验证码校验成功"),
        @ApiResponse(responseCode = "401", description = "验证码错误或已过期")
    })
    @PostMapping("/check")
    public Result<?> checkVerifyCode(@RequestBody VerifyCodeCheckRequest request) {
        return verifyCodeService.checkVerifyCode(request.getPhone(), request.getVerifyCode(), request.getScene());
    }
}

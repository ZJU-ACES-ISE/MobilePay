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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "验证码相关接口", description = "验证码发送和校验")
@Validated
@RestController
@RequestMapping("/verifyCode")
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 发送验证码
     * @param phone 手机号
     * @param scene 场景，register-注册，login-登录，resetPassword-重置密码
     * @return 验证码信息
     */
    @Operation(summary = "发送验证码", description = "向指定手机号发送验证码，支持注册、登录和重置密码场景")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "发送成功"),
        @ApiResponse(responseCode = "409", description = "手机号已被注册（注册场景）"),
        @ApiResponse(responseCode = "404", description = "手机号未注册（登录场景和重置密码场景）")
    })
    @GetMapping("/send")
    public Result<VerifyCodeVo> sendVerifyCode(
            @Parameter(description = "手机号", required = true) 
            @RequestParam String phone,
            @Parameter(description = "场景，register-注册，login-登录，resetPassword-重置密码", required = true) 
            @RequestParam String scene) {
        return verifyCodeService.sendVerifyCode(phone, scene);
    }
    
    /**
     * 校验验证码
     * @param request 校验请求
     * @return 校验结果
     */
    @Operation(summary = "校验验证码", description = "校验用户输入的验证码是否正确")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "校验成功"),
        @ApiResponse(responseCode = "401", description = "验证码错误或已过期")
    })
    @PostMapping("/check")
    public Result<?> checkVerifyCode(@RequestBody VerifyCodeCheckRequest request) {
        return verifyCodeService.checkVerifyCode(request.getPhone(), request.getVerifyCode(), request.getScene());
    }
}

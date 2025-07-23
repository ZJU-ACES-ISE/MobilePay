package org.software.code.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.software.code.service.UserService;
import org.software.code.common.result.Result;


@Tag(name = "用户相关接口", description = "用户登录、登出、信息查询等操作")
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
     * @return 统一返回结构
     */
    @GetMapping("/sendVerifyCode")
    public Result<?> sendVerifyCode(@RequestParam String phone,
                                    @RequestParam String scene,
                                    @RequestHeader("Authorization") String token) {
        return userService.sendVerifyCode(phone, scene, token);
    }
}

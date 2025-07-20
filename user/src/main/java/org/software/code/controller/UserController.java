package org.software.code.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "用户相关接口", description = "用户登录、登出、信息查询等操作")
@Validated
@RestController
@RequestMapping("/user")
public class UserController {


}

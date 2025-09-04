package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 用户服务Feign客户端
 * 用于appPayment模块调用appUser服务
 */
@FeignClient(name = "appUser", path = "/user")
public interface UserClient {

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    Map<String, Object> getUserById(@PathVariable("userId") Long userId);

    /**
     * 验证用户支付密码
     */
    @PostMapping("/{userId}/verify-pay-password")
    Boolean verifyPayPassword(@PathVariable("userId") Long userId,
                            @RequestParam("payPassword") String payPassword);

    /**
     * 获取用户基本信息
     */
    @GetMapping("/{userId}/basic")
    Map<String, Object> getUserBasicInfo(@PathVariable("userId") Long userId);

    /**
     * 获取用户昵称
     */
    @GetMapping("/{userId}/nickname")
    String getUserNickname(@PathVariable("userId") Long userId);
} 
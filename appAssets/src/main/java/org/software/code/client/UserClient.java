package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 用户服务Feign客户端
 * 用于appAssets模块调用appUser服务
 */
@FeignClient(name = "appUser", path = "/user")
public interface UserClient {

    /**
     * 验证支付密码
     */
    @PostMapping("/{userId}/verify-pay-password")
    Boolean verifyPayPassword(@PathVariable("userId") Long userId, 
                            @RequestParam("payPassword") String payPassword);

    /**
     * 获取用户昵称
     */
    @GetMapping("/{userId}/nickname")
    String getUserNickname(@PathVariable("userId") Long userId);
} 
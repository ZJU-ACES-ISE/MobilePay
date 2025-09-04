package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * 资产服务Feign客户端
 * 用于appUser模块调用appAssets服务
 */
@FeignClient(name = "appAssets", path = "/assets")
public interface AssetsClient {

    /**
     * 创建用户余额记录
     */
    @PostMapping("/user-balance")
    Boolean createUserBalance(@RequestParam("userId") Long userId, 
                            @RequestParam("balance") BigDecimal balance);
} 
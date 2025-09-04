package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * 资产服务Feign客户端
 * 用于appPayment模块调用appAssets服务
 */
@FeignClient(name = "appAssets", path = "/assets")
public interface AssetsClient {

    /**
     * 获取用户余额
     */
    @GetMapping("/balance/{userId}")
    BigDecimal getUserBalance(@PathVariable("userId") Long userId);

    /**
     * 扣减用户余额
     */
    @PostMapping("/balance/{userId}/deduct")
    Boolean deductBalance(@PathVariable("userId") Long userId,
                        @RequestParam("amount") BigDecimal amount,
                        @RequestParam("description") String description);

    /**
     * 增加用户余额
     */
    @PostMapping("/balance/{userId}/add")
    Boolean addBalance(@PathVariable("userId") Long userId,
                     @RequestParam("amount") BigDecimal amount,
                     @RequestParam("description") String description);

    /**
     * 创建交易记录
     */
    @PostMapping("/transaction-record")
    Boolean createTransactionRecord(@RequestBody Object transactionRecord);
} 
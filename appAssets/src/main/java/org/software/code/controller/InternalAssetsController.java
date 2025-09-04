package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.entity.BankCard;
import org.software.code.entity.TransferRecord;
import org.software.code.service.AssetsService;
import org.software.code.vo.BalanceSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 资产服务内部API控制器
 * 用于处理来自其他服务的Feign调用
 */
@RestController
@RequestMapping("/assets")
public class InternalAssetsController {

    @Autowired
    private AssetsService assetsService;

    /**
     * 获取用户余额信息
     */
    @GetMapping("/balance/{userId}")
    public BigDecimal getUserBalance(@PathVariable("userId") Long userId) {
        return assetsService.getUserBalance(userId);
    }

    /**
     * 扣减用户余额
     */
    @PostMapping("/balance/{userId}/deduct")
    public Boolean deductBalance(@PathVariable("userId") Long userId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("description") String description) {
        try {
            assetsService.deductBalance(userId, amount, description);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 增加用户余额
     */
    @PostMapping("/balance/{userId}/add")
    public Boolean addBalance(@PathVariable("userId") Long userId,
                            @RequestParam("amount") BigDecimal amount,
                            @RequestParam("description") String description) {
        try {
            assetsService.addBalance(userId, amount, description);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取用户资产统计
     */
    @GetMapping("/statistics/{userId}")
    public BalanceSummaryVo getUserAssetsStatistics(@PathVariable("userId") Long userId) {
        return assetsService.getUserAssetsStatistics(userId);
    }

    /**
     * 获取银行卡信息
     */
    @GetMapping("/cards/{userId}")
    public List<BankCard> getUserCards(@PathVariable("userId") Long userId) {
        return assetsService.getUserCards(userId);
    }

    /**
     * 创建交易记录
     */
    @PostMapping("/transaction-record")
    public Boolean createTransactionRecord(@RequestBody Object transactionRecord) {
        try {
            assetsService.createTransactionRecord(transactionRecord);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 创建用户余额记录
     */
    @PostMapping("/user-balance")
    public Boolean createUserBalance(@RequestParam("userId") Long userId, 
                                   @RequestParam("balance") BigDecimal balance) {
        Result<Boolean> result = assetsService.createUserBalance(userId, balance);
        return result.getData();
    }
} 
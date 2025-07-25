package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.software.code.common.result.Result;
import org.software.code.dto.BankTransferDto;
import org.software.code.service.AssetsService;
import org.software.code.vo.BalanceSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "资产相关接口", description = "银行卡、充值、提现等操作")
@Validated
@RestController
@RequestMapping("/assets")
public class AssetsController {
    @Autowired
    private AssetsService assetsService;

    /**
     * 银行卡充值接口
     *
     * @param bankTransferDto 充值银行卡请求数据
     * @return 操作结果
     */
    @Operation(summary = "用户余额充值", description = "输入金额并提交转入请求，校验支付密码和银行卡余额")
    @PostMapping("/topup")
    public Result<?> topUp(
            @RequestBody @Validated BankTransferDto bankTransferDto) {
        assetsService.topUp(bankTransferDto);
        return Result.success("余额充值成功");
    }

    /**
     * 银行卡提现接口
     *
     * @param bankTransferDto 提现银行卡请求数据
     * @return 操作结果
     */
    @PostMapping("/withdraw")
    @Operation(summary = "用户余额提现", description = "输入金额并提交转入请求，校验支付密码和用户余额")
    public Result<?> withdraw(@RequestBody @Validated BankTransferDto bankTransferDto) {
        assetsService.withdraw(bankTransferDto);
        return Result.success("余额提现成功");
    }

    /**
     * 总资产展示接口
     *
     * @param uid Header中的uid
     * @return 操作结果
     */
    @GetMapping("/summary")
    @Operation(summary = "总资产展示")
    public Result<BalanceSummaryVo> getSummary(
            @Parameter(description = "Bearer 类型 Token携带的uid", required = true)
            @RequestHeader("X-User-Id") Long uid) {
        BalanceSummaryVo vo = assetsService.getBalanceSummary(uid);
        return Result.success(vo);
    }
}

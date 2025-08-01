package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.dto.SetAmountDto;
import org.software.code.service.ReceiptService;
import org.software.code.vo.ReceiptCodeVo;
import org.software.code.vo.ReceiptConfirmVo;
import org.software.code.vo.ReceiptRecordsVo;
import org.software.code.vo.SetAmountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "收款码相关接口", description = "收款码生成、刷新等操作")
@Validated
@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    /**
     * 获取收款码（刷新收款码）
     * @param authorization Bearer类型Token认证
     * @return 收款码信息
     */
    @Operation(summary = "获取收款码（刷新收款码）", description = "用于获取用户的收款码，支持指定收款金额")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "Token无效或已过期"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/code")
    public Result<ReceiptCodeVo> getReceiptCode(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "收款金额，可选参数，默认为0")
            @RequestParam(value = "amount", required = false) BigDecimal amount) {
        if (amount != null) {
            return receiptService.getReceiptCode(authorization, amount);
        } else {
            return receiptService.getReceiptCode(authorization);
        }
    }

    @Operation(summary = "设置收款金额", description = "设置收款码的固定金额。特殊说明一下：如果没有设置收款金额，前端要给扫码的人提供一个输入金额的入口；如果有设置金额，就按照设置的金额直接提供给用户信息（不能再次修改）")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "金额设置成功"),
        @ApiResponse(responseCode = "400", description = "无效请求或收款码状态不允许设置金额"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/code/set-amount")
    public Result<SetAmountVo> setReceiptAmount(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Validated @RequestBody SetAmountDto setAmountDto) {
        return receiptService.setReceiptAmount(authorization, setAmountDto);
    }
    
    /**
     * 收款成功响应
     * @param authorization Bearer类型Token认证
     * @param transactionId 交易流水号
     * @return 交易确认信息
     */
    @Operation(summary = "收款成功响应", description = "确认收款交易并返回交易详情")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "收款成功"),
        @ApiResponse(responseCode = "400", description = "无效请求或交易状态不允许操作"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/confirm")
    public Result<ReceiptConfirmVo> confirmReceipt(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "交易流水号", required = true)
            @RequestParam("transactionId") String transactionId) {
        return receiptService.confirmReceipt(authorization, transactionId);
    }
    
    /**
     * 查询所有收款记录
     * @param authorization Bearer类型Token认证
     * @return 收款记录列表
     */
    @Operation(summary = "查询所有收款记录", description = "查询用户收款到的所有款项")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "Token无效或已过期"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/records")
    public Result<ReceiptRecordsVo> getReceiptRecords(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization) {
        return receiptService.getReceiptRecords(authorization);
    }

    /**
     * 查询最近几条收款记录
     * @param authorization Bearer类型Token认证
     * @param limit 查询记录的数量，默认为3条
     * @return 收款记录列表
     */
    @Operation(summary = "查询最近几条收款记录", description = "查询最近几条的收款记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "Token无效或已过期"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/recent")
    public Result<ReceiptRecordsVo> getRecentReceiptRecords(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "查询记录的数量，默认为3条", required = false)
            @RequestParam(value = "limit", required = false) Integer limit) {
        return receiptService.getRecentReceiptRecords(authorization, limit);
    }
}

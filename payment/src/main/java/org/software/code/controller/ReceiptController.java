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

    @Operation(summary = "设置收款金额", description = "设置收款码的固定金额")
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
}

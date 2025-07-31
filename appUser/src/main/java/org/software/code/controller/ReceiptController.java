package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.service.ReceiptService;
import org.software.code.vo.ReceiptCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "获取收款码（刷新收款码）", description = "用于获取用户的收款码")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "Token无效或已过期"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/code")
    public Result<ReceiptCodeVo> getReceiptCode(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization) {
        return receiptService.getReceiptCode(authorization);
    }
}

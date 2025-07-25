package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.software.code.common.result.Result;
import org.software.code.service.BillsService;
import org.software.code.vo.BillsSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "账单相关接口", description = "账单总览、明细展示等操作")
@Validated
@RestController
@RequestMapping("/assets/bills")
public class BillsController {
    @Autowired
    private BillsService billsService;

    @Operation(summary = "账单总览展示", description = "根据年月展示总收入、总支出、结余、各类支出明细")
    @GetMapping("/summary")
    public Result<BillsSummaryVo> getBillsSummary(
            @Parameter(description = "Bearer 类型 Token携带的uid", required = true)
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "年份", example = "2025") @RequestParam String year,
            @Parameter(description = "月份", example = "7") @RequestParam String month
    ) {
        BillsSummaryVo vo = billsService.getBillsSummary(userId, year, month);
        return Result.success(vo);
    }
}

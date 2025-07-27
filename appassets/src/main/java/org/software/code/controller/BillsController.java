package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.software.code.common.result.Result;
import org.software.code.service.BillsService;
import org.software.code.vo.BillsDetailVo;
import org.software.code.vo.BillsListVo;
import org.software.code.vo.BillsSummaryVo;
import org.software.code.vo.YearBillsSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "账单相关接口", description = "账单总览、明细展示等操作")
@Validated
@RestController
@RequestMapping("/assets/bills")
public class BillsController {
    @Autowired
    private BillsService billsService;
    /**
     * 账单总览展示
     *
     * @param userId 用户id
     * @param year 年
     * @param month 月
     *
     * @return 操作结果
     */
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

    /**
     * 账单明细数据列表展示
     *
     * @param userId 用户id
     *
     * @return 操作结果
     */
    @Operation(summary = "账单明细展示")
    @GetMapping("/list")
    public Result<List<BillsListVo>> getBillsList(
            @Parameter(description = "Bearer 类型 Token携带的uid", required = true)
            @RequestHeader("X-User-Id") Long userId) {
        List<BillsListVo> list = billsService.getBillsList(userId, null,null, null,null,null, null);
        return Result.success(list);
    }

    /**
     * 条件筛选 账单明细数据列表展示
     *
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 账单类型
     * @param scene 场景
     *
     * @return 操作结果
     */
    @Operation(summary = "条件筛选账单明细展示")
    @GetMapping("/list/condition")
    public Result<List<BillsListVo>> getBillsListByCondition(
            @Parameter(description = "Bearer 类型 Token携带的uid", required = true)
            @RequestHeader("X-User-Id") Long userId,

            @Parameter(description = "开始时间，如 2025-06-01 00:00:00", example = "2025-06-01 00:00:00")
            @RequestParam(required = false) String startTime,

            @Parameter(description = "结束时间，如 2025-06-30 23:59:59", example = "2025-06-30 23:59:59")
            @RequestParam(required = false) String endTime,

            @Parameter(description = "账单类型：1=收入，2=转出", example = "1")
            @RequestParam(required = false) Integer type,

            @Parameter(description = "场景：如 bills 或 index", example = "bills")
            @RequestParam(required = false) String scene) {
        List<BillsListVo> list = billsService.getBillsList(userId, startTime, endTime, type, scene, null, null);
        return Result.success(list);
    }

    /**
     * 模糊搜索 账单明细数据列表展示
     *
     * @param userId 用户id
     * @param keyString 模糊搜索关键词
     * @param scene 场景
     *
     * @return 操作结果
     */
    @Operation(summary = "账单模糊搜索")
    @GetMapping("/search")
    public Result<List<BillsListVo>> searchBills(
            @Parameter(description = "Bearer 类型 Token携带的用户ID", required = true)
            @RequestHeader("X-User-Id") Long userId,

            @Parameter(description = "场景：如 bills 或 index", example = "bills")
            @RequestParam(required = false) String scene,

            @Parameter(description = "模糊搜索关键词，如 星巴克、张三、招商银行")
            @RequestParam(required = false) String keyString
    ) {
        List<BillsListVo> list = billsService.getBillsList(userId, null, null, null, scene, keyString, null);
        return Result.success(list);
    }

    /**
     * 近期交易 账单明细数据列表展示
     *
     * @param userId 用户id
     * @param num 需要返回的近期账单数量
     * @param scene 场景
     *
     * @return 操作结果
     */
    @Operation(summary = "首页展示近期交易")
    @GetMapping("/list/index")
    public Result<List<BillsListVo>> getRecentBillsForIndex(
            @Parameter(description = "Bearer 类型 Token携带的用户ID", required = true)
            @RequestHeader("X-User-Id") Long userId,

            @Parameter(description = "需要返回的近期账单数量", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer num,

            @Parameter(description = "接口使用场景，如 index", example = "index")
            @RequestParam(required = false, defaultValue = "index") String scene
    ) {
        List<BillsListVo> list = billsService.getBillsList(userId, null,null, null,scene,null, num);
        return Result.success(list);
    }

    /**
     * 年度账单总览展示
     *
     * @param userId 用户id
     * @param year 年份
     *
     * @return 操作结果
     */
    @Operation(summary = "年度账单总览展示")
    @GetMapping("/yearSummary")
    public Result<YearBillsSummaryVo> getYearSummary(
            @Parameter(description = "Bearer 类型 Token携带的uid", required = true)
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "年份，如2025", example = "2025")
            @RequestParam(required = true) String year
    ) {
        YearBillsSummaryVo summary = billsService.getYearSummary(userId, year);
        return Result.success(summary);
    }

    /**
     * 年度账单总览展示
     *
     * @param id 账单主键 ID
     *
     * @return 操作结果
     */
    @Operation(summary = "根据 ID 获取账单详情")
    @GetMapping("/detail")
    public Result<BillsDetailVo> getBillDetail(
            @Parameter(description = "账单主键 ID", required = true)
            @RequestParam Long id) {
        BillsDetailVo detailVo = billsService.getBillsDetailById(id);
        return Result.success(detailVo);
    }

    /**
     * 修改账单备注
     *
     * @param id 账单主键 ID
     * @param remark 备注内容
     *
     * @return 操作结果
     */
    @Operation(summary = "修改账单备注")
    @PutMapping("/remark")
    public Result<?> updateRemark(
            @Parameter(description = "账单主键ID", required = true, example = "100001")
            @RequestParam Long id,
            @Parameter(description = "备注内容", required = true, example = "备注内容")
            @RequestParam String remark) {

        billsService.updateRemarkById(id, remark);
        return Result.success("账单备注修改成功", null);
    }

    /**
     * 修改账单支出分类
     *
     * @param id 账单主键 ID
     * @param bizCatogry 交易分类
     *
     * @return 操作结果
     */
    @Operation(summary = "修改账单支出分类")
    @PutMapping("/bizCatogry")
    public Result<?> updateBizCategory(
            @Parameter(description = "账单流水ID", required = true)
            @RequestParam Long id,
            @Parameter(description = "交易分类（1=餐饮，2=出行，3=购物，4=其他）", required = true)
            @RequestParam Integer bizCatogry) {

        billsService.updateBizCategoryById(id, bizCatogry);
        return Result.success("账单类别修改成功", null);
    }

}

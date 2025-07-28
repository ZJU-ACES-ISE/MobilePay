package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "年度账单汇总视图")
public class YearBillsSummaryVo {
    @Schema(description = "用户唯一标识", example = "123456789")
    private Long id;

    @Schema(description = "总收入", example = "180000.75")
    private String totalIncome;

    @Schema(description = "总支出", example = "120000.5")
    private String totalExpense;

    @Schema(description = "总交易次数", example = "345")
    private Integer totalCount;

    @Schema(description = "分类支出统计")
    private List<ExpenseCategoryVo> expenseVos;

    @Schema(description = "折线图数据")
    private List<ExpenseChartVo> expenseChartVos;
}

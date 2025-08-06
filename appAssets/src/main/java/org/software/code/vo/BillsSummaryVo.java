package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "账单总览展示数据")
public class BillsSummaryVo {

    @Schema(description = "用户唯一标识")
    private String id;

    @Schema(description = "本月总收入", example = "800.00")
    private String totalIncome;

    @Schema(description = "本月总支出", example = "650.00")
    private String totalExpense;

    @Schema(description = "结余 = 收入 - 支出", example = "150.00")
    private String balance;

    @Schema(description = "各类支出占比")
    private List<ExpenseCategoryVo> expenseV0;
}

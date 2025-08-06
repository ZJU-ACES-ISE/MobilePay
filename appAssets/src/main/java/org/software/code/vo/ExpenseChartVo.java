package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "月份支出折线图数据")
public class ExpenseChartVo {

    @Schema(description = "月份", example = "1")
    private Integer month;

    @Schema(description = "该月支出金额", example = "10000.5")
    private String totalExpense;
}

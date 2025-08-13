package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分类支出明细")
public class ExpenseCategoryVo {

    @Schema(description = "交易分类", example = "1")
    private Integer bizCategory;

    @Schema(description = "交易分类名称", example = "餐饮")
    private String bizCategoryName;

    @Schema(description = "该分类总支出", example = "300.00")
    private String amount;

    @Schema(description = "支出占比百分比", example = "46.15")
    private String percentage;
}

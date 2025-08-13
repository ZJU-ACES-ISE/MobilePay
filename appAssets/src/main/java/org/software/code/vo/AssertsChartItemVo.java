package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "资产折线图单项数据")
@Data
public class AssertsChartItemVo {

    @Schema(description = "日期", example = "07-20")
    private String day;

    @Schema(description = "对应日期的余额（元）", example = "1200.50")
    private String balance;
}
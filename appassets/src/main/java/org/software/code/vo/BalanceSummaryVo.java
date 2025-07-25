package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "总资产展示数据")
@Data
public class BalanceSummaryVo {

    @Schema(description = "用户余额唯一标识", example = "1")
    private Long id;

    @Schema(description = "余额（元）", example = "1200.50")
    private String balance;

    @Schema(description = "余额最后更新时间", example = "2025-07-20T12:00:00")
    private String updateTime;

    @Schema(description = "最近七天余额数据列表")
    private List<AssertsChartItemVo> assertsChartV0;
}

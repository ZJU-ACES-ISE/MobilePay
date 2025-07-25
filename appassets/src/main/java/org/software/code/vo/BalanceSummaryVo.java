package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "总资产展示数据")
@Data
public class BalanceSummaryVo {

    @Schema(description = "用户余额唯一标识", example = "1")
    private Long id;

    @Schema(description = "余额（元）", example = "1200.50")
    private String balance;

    @Schema(description = "余额最后更新时间", example = "2025-07-20T12:00:00")
    private String updateTime;

    @Schema(description = "日期列表（逗号分隔）", example = "07-18,07-19,07-20,07-21,07-22,07-23,07-24")
    private String dayListString;

    @Schema(description = "余额列表（逗号分隔）", example = "900.50,950.50,1000.50,1050.50,1100.50,1150.50,1200.50")
    private String balanceListString;
}
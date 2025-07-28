package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "账单明细数据")
@Data
public class BillsListVo {

    @Schema(description = "交易流水 ID", example = "10001")
    private Long id;

    @Schema(description = "交易类型：1=收入，2=转出", example = "1")
    private Integer type;

    @Schema(description = "交易类型名称", example = "收入")
    private String typeName;

    @Schema(description = "交易分类", example = "2")
    private Integer bizCategory;

    @Schema(description = "交易分类名称", example = "出行")
    private String bizCategoryName;

    @Schema(description = "交易对象名称", example = "招商银行")
    private String targetName;

    @Schema(description = "交易对象类型：1=用户，2=商户，3=银行卡", example = "3")
    private Integer targetType;

    @Schema(description = "交易对象类型名称", example = "银行卡")
    private String targetTypeName;

    @Schema(description = "交易金额", example = "199.99")
    private String amount;

    @Schema(description = "交易完成时间", example = "2025-07-20T10:00:00")
    private String completeTime;

    @Schema(description = "交易备注")
    private String remark;
}

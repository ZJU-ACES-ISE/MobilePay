package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "账单详情数据类型")
public class BillsDetailVo extends BillsListVo {

    @Schema(description = "平台唯一流水编号", example = "39865f25-e391-44cc-b8f4-b984a274a091")
    private String transferNumber;
}
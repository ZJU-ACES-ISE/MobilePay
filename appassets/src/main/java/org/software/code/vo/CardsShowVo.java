package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * CardsShowVo 是银行卡展示视图对象
 * 用于前端展示卡类型、银行名称、尾号、是否默认等信息
 */
@Data
@Schema(name = "CardsShowVo", description = "银行卡展示视图对象，用于展示卡类型、银行名称、尾号、是否默认等信息")
public class CardsShowVo {

    @Schema(description = "银行卡唯一标识")
    private Long id;

    @Schema(description = "银行名称（如“招商银行”）")
    private String bankName;

    @Schema(description = "银行卡类型：1=储蓄卡，2=信用卡")
    private Integer type;

    @Schema(description = "银行卡类型名，对应 type：1=储蓄卡，2=信用卡")
    private String typeName;

    @Schema(description = "是否为默认卡：1=默认，2=非默认")
    private Integer status;

    @Schema(description = "卡号尾号，用于展示（自动生成字段）")
    private String lastFourDigits;
}

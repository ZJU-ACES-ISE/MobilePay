package org.software.code.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class SetAmountDto {

    @NotBlank(message = "收款码ID不能为空")
    private String receiptCodeId;

    @NotBlank(message = "金额不能为空")
    private String amount;
}

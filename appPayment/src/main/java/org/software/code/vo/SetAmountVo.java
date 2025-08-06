package org.software.code.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetAmountVo {
    private Long userId;
    private String receiptCodeId;
    private String codeUrl;
    private String expireAt;
}

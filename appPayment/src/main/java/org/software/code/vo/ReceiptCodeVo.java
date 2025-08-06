package org.software.code.vo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 收款码视图对象(View Object)，用于返回给前端的收款码数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "收款码信息")
public class ReceiptCodeVo {
    
    /**
     * 用户ID
     */
    @Schema(description = "用户id", required = true, example = "789012")
    private Long userId;
    
    /**
     * 收款码ID
     */
    @Schema(description = "收款码的id", required = true, example = "RC12335421")
    private String receiptCodeId;
    
    /**
     * 二维码图片URL
     */
    @Schema(description = "二维码图片URL", required = true, example = "https://oss.example.com/qrcodes/RC12335421.png")
    private String qrCodeUrl;
    
    /**
     * 收款码数据（用于生成二维码的原始数据）
     */
    @Schema(description = "收款码数据", required = true)
    private String qrCodeData;
    
    /**
     * 二维码过期时间（时间戳）
     */
    @Schema(description = "二维码过期时间", required = true, example = "1640995200000")
    private Long expireAt;
    
    /**
     * 收款金额（字符串格式，便于前端显示）
     */
    @Schema(description = "收款金额", required = false, example = "100.00")
    private String amount;
}

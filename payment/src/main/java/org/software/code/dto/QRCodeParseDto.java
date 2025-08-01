package org.software.code.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 二维码解析请求DTO
 */
@Data
public class QRCodeParseDto {
    
    /**
     * 二维码图片（Base64编码或网络URL）
     */
    @NotBlank(message = "二维码图片不能为空")
    private String qrCode;
} 
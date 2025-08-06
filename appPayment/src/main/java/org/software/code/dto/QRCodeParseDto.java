package org.software.code.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 二维码解析请求DTO
 */
@Data
public class QRCodeParseDto {
    
    /**
     * 二维码图片URL
     */
    @NotBlank(message = "二维码图片URL不能为空")
    private String qrCode;
} 
package org.software.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 头像上传响应DTO
 */
@Data
@Schema(description = "头像上传响应")
public class AvatarUploadResponseVo {
    
    /**
     * 头像URL
     */
    @Schema(description = "用户头像URL", example = "https://example.com/avatar/123456.jpg")
    private String avatarUrl;
} 
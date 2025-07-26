package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户个人信息更新请求DTO
 */
@Data
@Schema(description = "用户个人信息更新请求")
public class UserProfileUpdateRequest {
    
    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "新昵称")
    private String nickName;
    
    /**
     * 手机号
     */
    @Schema(description = "电话", example = "13812345678")
    private String phone;
    
    /**
     * 头像URL
     */
    @Schema(description = "用户头像", example = "https://cdn.example.com/avatar/new.jpg")
    private String avatarUrl;
}

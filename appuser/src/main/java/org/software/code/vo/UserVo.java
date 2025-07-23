package org.software.code.vo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户视图对象(View Object)，用于返回给前端的用户数据
 * 不包含敏感字段，如密码等
 */
@Data
@Schema(description = "用户信息")
public class UserVo {
    /**
     * 用户ID
     */
    @Schema(description = "用户id", required = true)
    private String userId;
    
    /**
     * 手机号
     */
    @Schema(description = "用户电话号码", required = true)
    private String phone;
    
    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", required = true)
    private String nickName;
    
    /**
     * 头像URL
     */
    @Schema(description = "用户头像", required = true)
    private String avatarUrl;
} 
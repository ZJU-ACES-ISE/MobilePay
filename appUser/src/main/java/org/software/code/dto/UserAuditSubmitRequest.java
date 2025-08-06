package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户资料审核提交请求
 */
@Data
@Schema(description = "用户资料审核提交请求")
public class UserAuditSubmitRequest {

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Schema(description = "真实姓名", required = true, example = "张三")
    private String realName;
    
    /**
     * 身份证正面照片URL
     */
    @NotBlank(message = "身份证正面照片不能为空")
    @Schema(description = "身份证正面照片存储地址", required = true, example = "https://xxx.com/front.jpg")
    private String idCardFrontUrl;
    
    /**
     * 身份证号码
     */
    @NotBlank(message = "身份证号码不能为空")
    @Schema(description = "身份证号码", required = true, example = "110101199003070000")
    private String idCard;
    
    /**
     * 身份证背面照片URL
     */
    @NotBlank(message = "身份证背面照片不能为空")
    @Schema(description = "身份证背面照片存储地址", required = true, example = "https://xxx.com/back.jpg")
    private String idCardBackUrl;
} 
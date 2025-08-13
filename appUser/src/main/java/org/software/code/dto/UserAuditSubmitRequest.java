package org.software.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
     * 身份证正面照片文件
     */
    @NotNull(message = "身份证正面照片不能为空")
    @Schema(description = "身份证正面照片文件", required = true)
    private MultipartFile idCardFrontFile;
    
    /**
     * 身份证号码
     */
    @NotBlank(message = "身份证号码不能为空")
    @Schema(description = "身份证号码", required = true, example = "110101199003070000")
    private String idCard;
    
    /**
     * 身份证背面照片文件
     */
    @NotNull(message = "身份证背面照片不能为空")
    @Schema(description = "身份证背面照片文件", required = true)
    private MultipartFile idCardBackFile;
} 
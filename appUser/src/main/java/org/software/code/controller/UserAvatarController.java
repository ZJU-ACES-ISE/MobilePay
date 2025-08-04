package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.common.util.OSSUtil;
import org.software.code.service.UserService;
import org.software.code.vo.AvatarUploadResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "用户头像相关接口", description = "用户头像上传、更新等操作")
@RestController
@RequestMapping("/user/avatar")
public class UserAvatarController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private OSSUtil ossUtil;
    
    /**
     * 上传头像
     * @param token 用户token
     * @param avatarFile 头像文件
     * @return 头像URL
     */
    @Operation(summary = "上传用户头像", description = "上传头像图片到OSS并更新用户头像URL")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "上传成功"),
        @ApiResponse(responseCode = "400", description = "上传失败或文件格式不支持"),
        @ApiResponse(responseCode = "401", description = "Token无效或已过期")
    })
    @PostMapping("/upload")
    public Result<AvatarUploadResponseVo> uploadAvatar(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "头像图片文件", required = true)
            @RequestParam("file") MultipartFile avatarFile) {
        
        try {
            // 检查文件是否为空
            if (avatarFile.isEmpty()) {
                return Result.failed("头像文件不能为空", AvatarUploadResponseVo.class);
            }
            
            // 检查文件类型
            String contentType = avatarFile.getContentType();
            if (contentType == null || (!contentType.startsWith("image/"))) {
                return Result.failed("只支持上传图片文件", AvatarUploadResponseVo.class);
            }
            
            // 生成唯一的文件名
            String originalFilename = avatarFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // 使用UUID作为文件名，避免冲突
            String fileName = "avatar/" + UUID.randomUUID().toString() + extension;
            
            // 上传到OSS
            String avatarUrl = ossUtil.uploadBytes(avatarFile.getBytes(), fileName);
            
            // 更新用户头像URL
            userService.updateAvatarUrl(token, avatarUrl);
            
            // 返回结果
            AvatarUploadResponseVo responseDto = new AvatarUploadResponseVo();
            responseDto.setAvatarUrl(avatarUrl);
            
            return Result.success("头像上传成功", responseDto);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(400, "头像上传失败: " + e.getMessage(), null);
        }
    }
} 
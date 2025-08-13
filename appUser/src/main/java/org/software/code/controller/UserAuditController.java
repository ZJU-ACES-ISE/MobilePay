package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.common.util.OSSUtil;
import org.software.code.dto.UserAuditSubmitRequest;
import org.software.code.service.UserAuditService;
import org.software.code.vo.UserAuditStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "用户资料审核接口", description = "用户实名认证资料提交与审核状态查询")
@Validated
@RestController
@RequestMapping("/user/audit")
public class UserAuditController {

    @Autowired
    private UserAuditService userAuditService;
    
    @Autowired
    private OSSUtil ossUtil;
    
    /**
     * 提交资料审核
     * @param token 用户token
     * @param realName 真实姓名
     * @param idCard 身份证号码
     * @param idCardFrontFile 身份证正面照片文件
     * @param idCardBackFile 身份证背面照片文件
     * @return 提交结果
     */
    @Operation(summary = "资料审核", description = "用户提交实名认证资料，包括姓名、身份证正反面图片等，后台收到后将状态设为\"待审核\"")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "资料提交成功，等待审核"),
        @ApiResponse(responseCode = "400", description = "缺少必填参数或文件格式不支持"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/submit")
    public Result<?> submitAudit(
            @Parameter(description = "Bearer 类型 Token 认证", required = true) 
            @RequestHeader("Authorization") String token,
            @Parameter(description = "真实姓名", required = true)
            @RequestParam("realName") String realName,
            @Parameter(description = "身份证号码", required = true)
            @RequestParam("idCard") String idCard,
            @Parameter(description = "身份证正面照片文件", required = true)
            @RequestParam("idCardFrontFile") MultipartFile idCardFrontFile,
            @Parameter(description = "身份证背面照片文件", required = true)
            @RequestParam("idCardBackFile") MultipartFile idCardBackFile) {
        
        try {
            // 参数验证
            if (realName == null || realName.trim().isEmpty()) {
                return Result.failed("真实姓名不能为空");
            }
            if (idCard == null || idCard.trim().isEmpty()) {
                return Result.failed("身份证号码不能为空");
            }
            
            // 检查文件是否为空
            if (idCardFrontFile.isEmpty()) {
                return Result.failed("身份证正面照片不能为空");
            }
            if (idCardBackFile.isEmpty()) {
                return Result.failed("身份证背面照片不能为空");
            }
            
            // 检查文件大小（限制为5MB）
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (idCardFrontFile.getSize() > maxSize) {
                return Result.failed("身份证正面照片文件大小不能超过5MB");
            }
            if (idCardBackFile.getSize() > maxSize) {
                return Result.failed("身份证背面照片文件大小不能超过5MB");
            }
            
            // 检查文件类型
            String frontContentType = idCardFrontFile.getContentType();
            String backContentType = idCardBackFile.getContentType();
            if (frontContentType == null || !frontContentType.startsWith("image/")) {
                return Result.failed("身份证正面照片必须是图片文件");
            }
            if (backContentType == null || !backContentType.startsWith("image/")) {
                return Result.failed("身份证背面照片必须是图片文件");
            }
            
            // 生成唯一的文件名
            String frontExtension = getFileExtension(idCardFrontFile.getOriginalFilename());
            String backExtension = getFileExtension(idCardBackFile.getOriginalFilename());
            
            String frontFileName = "audit/" + UUID.randomUUID().toString() + "_front" + frontExtension;
            String backFileName = "audit/" + UUID.randomUUID().toString() + "_back" + backExtension;
            
            // 上传到OSS
            String frontUrl = ossUtil.uploadBytes(idCardFrontFile.getBytes(), frontFileName);
            String backUrl = ossUtil.uploadBytes(idCardBackFile.getBytes(), backFileName);
            
            // 调用服务层处理业务逻辑
            return userAuditService.submitAudit(token, realName, idCard, frontUrl, backUrl);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("资料提交失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return ".jpg"; // 默认扩展名
    }
    
    /**
     * 查询审核状态
     * @param token 用户token
     * @return 审核状态信息
     */
    @Operation(summary = "审核状态查询", description = "查询当前用户实名认证审核状态及驳回原因（如有）")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "查询失败")
    })
    @GetMapping("/status")
    public Result<UserAuditStatusVo> getAuditStatus(
            @Parameter(description = "Bearer 类型 Token 认证", required = true) 
            @RequestHeader("Authorization") String token) {
        return userAuditService.getAuditStatus(token);
    }
} 
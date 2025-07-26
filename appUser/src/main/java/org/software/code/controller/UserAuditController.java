package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.dto.UserAuditSubmitRequest;
import org.software.code.service.UserAuditService;
import org.software.code.vo.UserAuditStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户资料审核接口", description = "用户实名认证资料提交与审核状态查询")
@Validated
@RestController
@RequestMapping("/user/audit")
public class UserAuditController {

    @Autowired
    private UserAuditService userAuditService;
    
    /**
     * 提交资料审核
     * @param token 用户token
     * @param request 审核提交请求
     * @return 提交结果
     */
    @Operation(summary = "资料审核", description = "用户提交实名认证资料，包括姓名、身份证正反面图片等，后台收到后将状态设为\"待审核\"")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "资料提交成功，等待审核"),
        @ApiResponse(responseCode = "400", description = "缺少必填参数")
    })
    @PostMapping("/submit")
    public Result<?> submitAudit(
            @Parameter(description = "Bearer 类型 Token 认证", required = true) 
            @RequestHeader("Authorization") String token,
            @RequestBody @Validated UserAuditSubmitRequest request) {
        return userAuditService.submitAudit(token, request);
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
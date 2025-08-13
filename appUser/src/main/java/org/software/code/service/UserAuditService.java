package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.UserAuditSubmitRequest;
import org.software.code.vo.UserAuditStatusVo;

/**
 * 用户资料审核服务接口
 */
public interface UserAuditService {
    
    /**
     * 提交资料审核（使用文件上传后的URL）
     * @param token JWT令牌
     * @param realName 真实姓名
     * @param idCard 身份证号码
     * @param idCardFrontUrl 身份证正面照片URL
     * @param idCardBackUrl 身份证背面照片URL
     * @return 提交结果
     */
    Result<?> submitAudit(String token, String realName, String idCard, String idCardFrontUrl, String idCardBackUrl);
    /**
     * 查询当前用户审核状态（详细版）
     * @param token JWT令牌
     * @return 审核状态
     */
    Result<UserAuditStatusVo> getAuditStatus(String token);

} 
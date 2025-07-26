package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.UserAuditSubmitRequest;
import org.software.code.vo.UserAuditStatusVo;

/**
 * 用户资料审核服务接口
 */
public interface UserAuditService {
    
    /**
     * 提交资料审核
     * @param token JWT令牌
     * @param request 审核提交请求
     * @return 提交结果
     */
    Result<?> submitAudit(String token, UserAuditSubmitRequest request);
    
    /**
     * 查询当前用户审核状态（详细版）
     * @param token JWT令牌
     * @return 审核状态
     */
    Result<UserAuditStatusVo> getAuditStatus(String token);

} 
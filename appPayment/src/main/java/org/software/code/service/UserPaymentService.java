package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.vo.UserBalanceVo;

/**
 * 用户服务接口
 */
public interface UserPaymentService {
    
    /**
     * 查询用户余额
     * @param authorization Bearer类型Token认证
     * @return 用户余额信息
     */
    Result<UserBalanceVo> getUserBalance(String authorization);
} 
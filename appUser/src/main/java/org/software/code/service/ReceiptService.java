package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.vo.ReceiptCodeVo;

/**
 * 收款码服务接口
 */
public interface ReceiptService {
    
    /**
     * 获取收款码（刷新收款码）
     * @param authorization Bearer类型Token认证
     * @return 收款码信息
     */
    Result<ReceiptCodeVo> getReceiptCode(String authorization);
}

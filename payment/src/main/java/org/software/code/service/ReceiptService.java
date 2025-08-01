package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.SetAmountDto;
import org.software.code.vo.ReceiptCodeVo;
import org.software.code.vo.SetAmountVo;

import java.math.BigDecimal;

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
    
    /**
     * 获取收款码（刷新收款码）带金额
     * @param authorization Bearer类型Token认证
     * @param amount 收款金额
     * @return 收款码信息
     */
        Result<ReceiptCodeVo> getReceiptCode(String authorization, BigDecimal amount);

    /**
     * 设置收款码金额
     * @param authorization Bearer类型Token认证
     * @param setAmountDto  设置金额的DTO
     * @return 设置金额后的收款码信息
     */
    Result<SetAmountVo> setReceiptAmount(String authorization, SetAmountDto setAmountDto);
}

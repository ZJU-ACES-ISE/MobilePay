package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.SetAmountDto;
import org.software.code.vo.ReceiptCodeVo;
import org.software.code.vo.ReceiptConfirmVo;
import org.software.code.vo.ReceiptRecordsVo;
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
    
    /**
     * 收款成功响应
     * @param authorization Bearer类型Token认证
     * @param transactionId 交易流水号
     * @return 交易确认信息
     */
    Result<ReceiptConfirmVo> confirmReceipt(String authorization, String transactionId);
    
    /**
     * 查询所有收款记录
     * @param authorization Bearer类型Token认证
     * @return 收款记录列表
     */
    Result<ReceiptRecordsVo> getReceiptRecords(String authorization);
    
    /**
     * 查询最近几条收款记录
     * @param authorization Bearer类型Token认证
     * @param limit 查询记录的数量，默认为3条
     * @return 收款记录列表
     */
    Result<ReceiptRecordsVo> getRecentReceiptRecords(String authorization, Integer limit);
}

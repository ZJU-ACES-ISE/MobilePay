package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.PaymentConfirmDto;
import org.software.code.dto.QRCodeParseDto;
import org.software.code.vo.PaymentConfirmVo;
import org.software.code.vo.QRCodeParseResultVo;

/**
 * 支付服务接口
 */
public interface PaymentService {
    
    /**
     * 解析二维码并返回支付信息
     * @param authorization Bearer类型Token认证（可选）
     * @param qrCodeParseDto 二维码解析请求
     * @return 支付信息
     */
    Result<QRCodeParseResultVo> parseQRCode(String authorization, QRCodeParseDto qrCodeParseDto);
    
    /**
     * 确认支付
     * @param authorization Bearer类型Token认证
     * @param paymentConfirmDto 支付确认请求
     * @return 支付结果
     */
    Result<PaymentConfirmVo> confirmPayment(String authorization, PaymentConfirmDto paymentConfirmDto);
} 
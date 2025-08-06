package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.software.code.common.result.Result;
import org.software.code.common.result.ResultEnum;
import org.software.code.dto.PaymentConfirmDto;
import org.software.code.dto.QRCodeParseDto;
import org.software.code.service.PaymentService;
import org.software.code.vo.PaymentConfirmVo;
import org.software.code.vo.QRCodeParseResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "支付相关接口", description = "扫码支付、支付处理等操作")
@Validated
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * 识别二维码并返回支付信息
     * @param authorization Bearer类型Token认证（可选）
     * @param imageUrl 二维码图片URL地址
     * @return 支付信息
     */
    @Operation(summary = "识别二维码并返回支付信息", description = "根据二维码URL识别出支付信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "识别成功"),
        @ApiResponse(responseCode = "400", description = "无效的图片URL或二维码格式不正确"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping(value = "/parse-code")
    public Result<QRCodeParseResultVo> parseQRCode(
            @Parameter(description = "Bearer 类型 Token 认证", required = false)
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "二维码图片URL地址", required = true)
            @RequestParam("imageUrl") String imageUrl) {
        try {
            logger.info("接收到二维码URL解析请求: {}", imageUrl);
            
            // 创建DTO对象
            QRCodeParseDto qrCodeParseDto = new QRCodeParseDto();
            qrCodeParseDto.setQrCode(imageUrl);
            
            logger.info("准备调用服务解析二维码");
            return paymentService.parseQRCode(authorization, qrCodeParseDto);
        } catch (Exception e) {
            logger.error("处理二维码URL时发生错误", e);
            return Result.instance(ResultEnum.FAILED.getCode(), "二维码处理失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 确认支付
     * @param authorization Bearer类型Token认证
     * @param paymentConfirmDto 支付确认请求
     * @return 支付结果
     */
    @Operation(summary = "确认支付", description = "用于确认支付功能")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "支付成功"),
        @ApiResponse(responseCode = "400", description = "参数错误或余额不足"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/confirm")
    public Result<PaymentConfirmVo> confirmPayment(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Validated @RequestBody PaymentConfirmDto paymentConfirmDto) {
        return paymentService.confirmPayment(authorization, paymentConfirmDto);
    }
} 
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
import org.software.code.dto.DiscountStrategyCreateDto;
import org.software.code.service.PaymentService;
import org.software.code.service.DiscountService;
import org.software.code.vo.PaymentConfirmVo;
import org.software.code.vo.QRCodeParseResultVo;
import org.software.code.vo.OrderContext;
import org.software.code.entity.DiscountStrategy;
import org.software.code.common.constants.DiscountConstants;
import org.software.code.common.util.JwtUtil;

import java.util.List;
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
    
    @Autowired
    private DiscountService discountService;

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
    
    /**
     * 查询可用的折扣策略列表
     * @param authorization Bearer类型Token认证
     * @param strategyType 策略类型（可选）
     * @return 折扣策略列表
     */
    @Operation(summary = "查询可用的折扣策略列表", description = "获取当前用户可用的折扣策略")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "401", description = "未授权"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/discount-strategies")
    public Result<List<DiscountStrategy>> getAvailableDiscountStrategies(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "策略类型", required = false)
            @RequestParam(required = false) String strategyType) {
        
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            List<DiscountStrategy> strategies;
            
            if (strategyType != null && !strategyType.trim().isEmpty()) {
                // 获取指定类型的策略
                DiscountStrategy strategy = discountService.getBestStrategy(strategyType, userId, null);
                strategies = strategy != null ? List.of(strategy) : List.of();
            } else {
                // 获取所有可用策略
                strategies = discountService.getAvailableStrategies(userId, null, null);
            }
            
            return Result.success("查询成功", strategies);
            
        } catch (Exception e) {
            logger.error("查询折扣策略时发生错误：{}", e.getMessage(), e);
            return Result.instance(ResultEnum.FAILED.getCode(), "查询失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 根据ID查询折扣策略详情
     * @param authorization Bearer类型Token认证
     * @param strategyId 策略ID
     * @return 折扣策略详情
     */
    @Operation(summary = "查询折扣策略详情", description = "根据策略ID获取折扣策略详细信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "策略不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/discount-strategies/{strategyId}")
    public Result<DiscountStrategy> getDiscountStrategyById(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "策略ID", required = true)
            @PathVariable Long strategyId) {
        
        try {
            DiscountStrategy strategy = discountService.getStrategyById(strategyId);
            
            if (strategy == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "折扣策略不存在或已失效", null);
            }
            
            return Result.success("查询成功", strategy);
            
        } catch (Exception e) {
            logger.error("查询折扣策略详情时发生错误，策略ID：{}，错误：{}", strategyId, e.getMessage(), e);
            return Result.instance(ResultEnum.FAILED.getCode(), "查询失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 新增折扣策略
     * @param authorization Bearer类型Token认证
     * @param createDto 创建折扣策略请求
     * @return 创建的折扣策略
     */
    @Operation(summary = "新增折扣策略", description = "创建新的折扣策略")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "参数错误或策略名称已存在"),
        @ApiResponse(responseCode = "401", description = "未授权"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/discount-strategies")
    public Result<DiscountStrategy> createDiscountStrategy(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "创建折扣策略请求", required = true)
            @Validated @RequestBody DiscountStrategyCreateDto createDto) {
        
        try {
            // 从token中获取用户ID作为创建者
            String token = authorization.replace("Bearer ", "");
            Long createdBy = JwtUtil.extractID(token);
            
            // 创建折扣策略
            DiscountStrategy strategy = discountService.createDiscountStrategy(createDto, createdBy);
            
            if (strategy == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "创建失败，策略名称可能已存在", null);
            }
            
            return Result.success("创建成功", strategy);
            
        } catch (Exception e) {
            logger.error("创建折扣策略时发生错误：{}", e.getMessage(), e);
            return Result.instance(ResultEnum.FAILED.getCode(), "创建失败: " + e.getMessage(), null);
        }
    }

} 
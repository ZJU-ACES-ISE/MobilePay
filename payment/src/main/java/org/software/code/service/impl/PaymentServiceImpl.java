package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.software.code.common.result.Result;
import org.software.code.common.result.ResultEnum;
import org.software.code.common.util.JwtUtil;
import org.software.code.dto.PaymentConfirmDto;
import org.software.code.dto.QRCodeParseDto;
import org.software.code.entity.UserBalance;
import org.software.code.mapper.UserBalanceMapper;
import org.software.code.service.PaymentService;
import org.software.code.vo.PaymentConfirmVo;
import org.software.code.vo.QRCodeParseResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 支付服务实现类
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserBalanceMapper userBalanceMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Result<QRCodeParseResultVo> parseQRCode(String authorization, QRCodeParseDto qrCodeParseDto) {
        try {
            // 解析图片URL
            if (qrCodeParseDto == null || qrCodeParseDto.getQrCode() == null || qrCodeParseDto.getQrCode().isEmpty()) {
                return Result.instance(ResultEnum.FAILED.getCode(), "请提供二维码图片", null);
            }

            // 读取图片
            BufferedImage bufferedImage;
            String qrCode = qrCodeParseDto.getQrCode();
            
            if (qrCode.startsWith("data:image")) {
                // 处理Base64编码的图片
                String base64Image = qrCode.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            } else {
                // 处理网络URL
                bufferedImage = ImageIO.read(new URL(qrCode));
            }
            
            if (bufferedImage == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "无效的图片格式", null);
            }

            // 使用ZXing库解析二维码
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            
            com.google.zxing.Result zxingResult;
            try {
                MultiFormatReader reader = new MultiFormatReader();
                zxingResult = reader.decode(bitmap, hints);
            } catch (NotFoundException e) {
                return Result.instance(ResultEnum.FAILED.getCode(), "未能识别二维码", null);
            }

            // 解析二维码中的JSON数据
            String qrCodeContent = zxingResult.getText();
            Map<String, Object> qrCodeData;
            try {
                qrCodeData = objectMapper.readValue(qrCodeContent, Map.class);
            } catch (Exception e) {
                return Result.instance(ResultEnum.FAILED.getCode(), "二维码数据格式不正确", null);
            }

            // 提取支付信息
            Long targetId = getLongValue(qrCodeData, "targetId");
            String targetName = getStringValue(qrCodeData, "targetName");
            Integer targetType = getIntegerValue(qrCodeData, "targetType");
            Integer bizCategory = getIntegerValue(qrCodeData, "bizCategory");
            Long userId = getLongValue(qrCodeData, "userId");
            
            // 解析金额信息，如果存在
            Integer amount = 0;
            if (qrCodeData.containsKey("amount")) {
                String amountStr = qrCodeData.get("amount").toString();
                try {
                    // 假设金额以元为单位，需要转换为分
                    double amountValue = Double.parseDouble(amountStr);
                    amount = (int)(amountValue * 100);
                } catch (NumberFormatException e) {
                    // 如果解析失败，使用默认值0
                }
            }
            
            // 计算折扣和实际金额（这里简单示例，实际可能需要调用其他服务计算）
            Integer discount = calculateDiscount(targetId, amount);
            Integer actualAmount = amount - discount;
            
            // 构建返回结果
            QRCodeParseResultVo resultVo = QRCodeParseResultVo.builder()
                    .targetId(targetId)
                    .targetName(targetName)
                    .targetType(targetType)
                    .bizCategory(bizCategory)
                    .userId(userId)
                    .amount(amount)
                    .discount(discount)
                    .actualAmount(actualAmount)
                    .build();
            
            return Result.success("识别成功", resultVo);
            
        } catch (IOException e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "图片处理失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    /**
     * 从Map中获取字符串值
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }
    
    /**
     * 从Map中获取Long值
     */
    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return 0L;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else {
                return Long.parseLong(value.toString());
            }
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    /**
     * 从Map中获取整数值
     */
    private Integer getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return 0;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            } else {
                return Integer.parseInt(value.toString());
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * 计算折扣（示例方法，实际可能需要调用优惠券服务等）
     */
    private Integer calculateDiscount(Long targetId, Integer amount) {
        // 这里只是示例，实际应该根据用户、商家、活动等信息计算折扣
        // 假设有10%的折扣
        return amount / 10;
    }
    
    @Override
    @Transactional
    public Result<PaymentConfirmVo> confirmPayment(String authorization, PaymentConfirmDto paymentConfirmDto) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = jwtUtil.extractID(token);
            
            // 验证交易类型
            if (paymentConfirmDto.getType() == 2 && paymentConfirmDto.getBizCategory() == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "支出交易必须指定分类", null);
            }
            
            // 解析金额
            BigDecimal amount;
            try {
                amount = new BigDecimal(paymentConfirmDto.getActualAmount());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    return Result.instance(ResultEnum.FAILED.getCode(), "金额必须大于0", null);
                }
            } catch (NumberFormatException e) {
                return Result.instance(ResultEnum.FAILED.getCode(), "金额格式不正确", null);
            }
            
            // 生成交易流水号
            String transactionId = generateTransactionId();
            
            // 从数据库获取用户余额
            QueryWrapper<UserBalance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            UserBalance userBalance = userBalanceMapper.selectOne(queryWrapper);
            
            if (userBalance == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "用户余额信息不存在", null);
            }
            
            BigDecimal balance = userBalance.getBalance();
            
            // 计算交易后余额
            BigDecimal balanceAfter;
            if (paymentConfirmDto.getType() == 1) {
                // 收入
                balanceAfter = balance.add(amount);
            } else {
                // 支出
                balanceAfter = balance.subtract(amount);
                if (balanceAfter.compareTo(BigDecimal.ZERO) < 0) {
                    return Result.instance(ResultEnum.FAILED.getCode(), "余额不足", null);
                }
            }
            
            // 更新用户余额
            userBalance.setBalance(balanceAfter);
            userBalance.setUpdateTime(LocalDateTime.now());
            userBalanceMapper.updateById(userBalance);
            
            // 记录交易（实际应该保存到数据库）
            // TODO: 保存交易记录到数据库
            
            // 生成交易完成时间
            LocalDateTime completeTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            
            // 构建返回结果
            PaymentConfirmVo confirmVo = PaymentConfirmVo.builder()
                    .transactionId(transactionId)
                    .balanceAfter(balanceAfter.toString())
                    .amount(amount.toString())
                    .type(paymentConfirmDto.getType())
                    .bizCategory(paymentConfirmDto.getBizCategory())
                    .targetId(paymentConfirmDto.getTargetId())
                    .targetName(paymentConfirmDto.getTargetName())
                    .targetType(paymentConfirmDto.getTargetType())
                    .completeTime(completeTime.format(formatter))
                    .build();
            
            return Result.success("支付成功", confirmVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    /**
     * 生成交易流水号
     */
    private String generateTransactionId() {
        return "T" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
} 
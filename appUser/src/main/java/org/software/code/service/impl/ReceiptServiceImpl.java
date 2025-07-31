package org.software.code.service.impl;

import org.software.code.common.result.Result;
import org.software.code.common.util.JwtUtil;
import org.software.code.common.util.QRCodeUtil;
import org.software.code.entity.ReceiptCode;
import org.software.code.entity.ReceiptTransaction;
import org.software.code.mapper.ReceiptCodeMapper;
import org.software.code.service.ReceiptService;
import org.software.code.vo.ReceiptCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 收款码服务实现类
 */
@Service
public class ReceiptServiceImpl implements ReceiptService {

    @Autowired
    private ReceiptCodeMapper receiptCodeMapper;

    @Autowired
    private QRCodeUtil qrCodeUtil;

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Result<ReceiptCodeVo> getReceiptCode(String authorization) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = jwtUtil.extractID(token);
            // 生成收款码ID
            String receiptCodeId = generateReceiptCodeId();
            
            // 设置过期时间（30分钟后过期）
            LocalDateTime expireTime = LocalDateTime.now().plusMinutes(30);
            long expireAt = expireTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // 构建收款码数据
            Map<String, Object> receiptData = new HashMap<>();
            receiptData.put("targetId", userId);
            receiptData.put("targetName", "收款用户"); // 这里可以从用户信息中获取真实姓名
            receiptData.put("targetType", 1); // 1表示个人用户
            receiptData.put("bizCategory", 1); // 1表示收款业务
            receiptData.put("userId", userId);
            receiptData.put("receiptCodeId", receiptCodeId);
            receiptData.put("expireAt", expireAt);

            String qrCodeData = objectMapper.writeValueAsString(receiptData);

            // 生成二维码图片并上传到OSS（这里先返回一个模拟的URL）
            String qrCodeUrl = qrCodeUtil.generateQRCodeAndUpload(qrCodeData, receiptCodeId);

            // 保存收款码信息到数据库
            ReceiptCode receiptCode = ReceiptCode.builder()
                    .receiptCodeId(receiptCodeId)
                    .userId(userId)
                    .codeUrl(qrCodeUrl)
                    .expireAt(expireTime)
                    .timestamp(LocalDateTime.now())
                    .build();

            receiptCodeMapper.insert(receiptCode);

            // 构建返回结果
            ReceiptCodeVo receiptCodeVo = ReceiptCodeVo.builder()
                    .userId(userId)
                    .receiptCodeId(receiptCodeId)
                    .qrCodeUrl(qrCodeUrl)
                    .qrCodeData(qrCodeData)
                    .expireAt(expireAt)
                    .build();

            return Result.success("查询成功", receiptCodeVo);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("内部错误", ReceiptCodeVo.class);
        }
    }

    /**
     * 生成收款码ID
     * @return 收款码ID
     */
    private String generateReceiptCodeId() {
        return "RC" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}

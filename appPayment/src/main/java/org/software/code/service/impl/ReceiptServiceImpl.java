package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.software.code.common.result.Result;
import org.software.code.dto.SetAmountDto;
import org.software.code.common.util.JwtUtil;
import org.software.code.common.util.QRCodeUtil;
import org.software.code.entity.ReceiptCode;
import org.software.code.entity.ReceiptTransaction;
import org.software.code.mapper.ReceiptCodeMapper;
import org.software.code.mapper.ReceiptTransactionMapper;
import org.software.code.mapper.UserMapper;
import org.software.code.service.ReceiptService;
import org.software.code.vo.ReceiptCodeVo;
import org.software.code.vo.ReceiptRecordVo;
import org.software.code.vo.ReceiptRecordsVo;
import org.software.code.vo.SetAmountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.entity.User;
import org.software.code.common.result.ResultEnum;
import org.software.code.vo.ReceiptConfirmVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private ReceiptTransactionMapper receiptTransactionMapper;

    @Autowired
    private QRCodeUtil qrCodeUtil;



    @Autowired(required = false)
    private UserMapper userMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Result<ReceiptCodeVo> getReceiptCode(String authorization) {
        return getReceiptCode(authorization, new BigDecimal(0));
    }
    
    @Override
    public Result<ReceiptCodeVo> getReceiptCode(String authorization, BigDecimal amount) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            // 生成收款码ID
            String receiptCodeId = generateReceiptCodeId();
            
            // 设置过期时间（30分钟后过期）
            LocalDateTime expireTime = LocalDateTime.now().plusMinutes(30);
            long expireAt = expireTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // 构建收款码数据
            Map<String, Object> receiptData = new HashMap<>();
            receiptData.put("targetId", userId);
            User user = userMapper.selectById(userId);
            String userName = (user != null && user.getNickname() != null) ? user.getNickname() : "收款用户";
            receiptData.put("targetName", userName);
            receiptData.put("targetType", 1); // 1表示个人用户
            receiptData.put("bizCategory", 1); // 1表示收款业务
            receiptData.put("userId", userId);
            receiptData.put("amount",amount);
            receiptData.put("receiptCodeId", receiptCodeId);
            receiptData.put("expireAt", expireAt);
            
            // 添加金额信息到二维码数据中
            if (amount != null) {
                receiptData.put("amount", amount.toString());
            }

            String qrCodeData = objectMapper.writeValueAsString(receiptData);

            // 生成二维码图片并上传到OSS
            String qrCodeUrl = qrCodeUtil.generateQRCodeAndUpload(qrCodeData, receiptCodeId);

            // 保存收款码信息到数据库
            ReceiptCode receiptCode = ReceiptCode.builder()
                    .receiptCodeId(receiptCodeId)
                    .userId(userId)
                    .codeUrl(qrCodeUrl)
                    .amount(amount)
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
                    .amount(amount != null ? amount.toString() : null)
                    .build();

            return Result.success("查询成功", receiptCodeVo);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("内部错误",ReceiptCodeVo.class);
        }
    }

    /**
     * 生成收款码ID
     * @return 收款码ID
     */
        private String generateReceiptCodeId() {
        return "RC" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public Result<SetAmountVo> setReceiptAmount(String authorization, SetAmountDto setAmountDto) {
        try {
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);

            QueryWrapper<ReceiptCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("receipt_code_id", setAmountDto.getReceiptCodeId());
            ReceiptCode receiptCode = receiptCodeMapper.selectOne(queryWrapper);

            if (receiptCode == null) {
                return Result.failed("收款码不存在", SetAmountVo.class);
            }
            if (!receiptCode.getUserId().equals(userId)) {
                return Result.failed("无权操作该收款码", SetAmountVo.class);
            }
            if (receiptCode.getExpireAt().isBefore(LocalDateTime.now())) {
                return Result.failed("收款码已过期", SetAmountVo.class);
            }
            if (receiptCode.getAmount() != null && receiptCode.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                return Result.failed("收款码已设置金额，不可重复设置", SetAmountVo.class);
            }

            // 将字符串金额转换为BigDecimal
            BigDecimal amount;
            try {
                amount = new BigDecimal(setAmountDto.getAmount());
            } catch (NumberFormatException e) {
                return Result.failed("金额格式不正确", SetAmountVo.class);
            }
            receiptCode.setAmount(amount);

            Map<String, Object> receiptData = new HashMap<>();
            receiptData.put("targetId", userId);
            User user = userMapper.selectById(userId);
            String userName = (user != null && user.getNickname() != null) ? user.getNickname() : "收款用户";
            receiptData.put("targetName", userName);
            receiptData.put("targetType", 1);
            receiptData.put("bizCategory", 1);
            receiptData.put("userId", userId);
            receiptData.put("receiptCodeId", receiptCode.getReceiptCodeId());
            receiptData.put("expireAt", receiptCode.getExpireAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            receiptData.put("amount", setAmountDto.getAmount());

            String qrCodeData = objectMapper.writeValueAsString(receiptData);
            String newQrCodeUrl = qrCodeUtil.generateQRCodeAndUpload(qrCodeData, receiptCode.getReceiptCodeId());
            
            receiptCode.setCodeUrl(newQrCodeUrl);
            receiptCodeMapper.updateById(receiptCode);

            SetAmountVo setAmountVo = SetAmountVo.builder()
                    .userId(userId)
                    .receiptCodeId(receiptCode.getReceiptCodeId())
                    .codeUrl(newQrCodeUrl)
                    .expireAt(String.valueOf(receiptCode.getExpireAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                    .build();

            return Result.success("金额设置成功", setAmountVo);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("服务器内部错误", SetAmountVo.class);
        }
    }

    @Override
    public Result<ReceiptConfirmVo> confirmReceipt(String authorization, String transactionId) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询交易记录是否存在
            QueryWrapper<ReceiptTransaction> transactionQueryWrapper = new QueryWrapper<>();
            transactionQueryWrapper.eq("transaction_id", transactionId);
            ReceiptTransaction transaction = receiptTransactionMapper.selectOne(transactionQueryWrapper);
            
            if (transaction == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "交易记录不存在", null);
            }
            
            // 验证收款人是否为当前用户
            if (!transaction.getReceiverId().equals(userId)) {
                return Result.instance(ResultEnum.FAILED.getCode(), "无权确认该交易", null);
            }
            
            // 查询收款码是否存在
            if (transaction.getReceiptCodeId() != null && !transaction.getReceiptCodeId().isEmpty()) {
                QueryWrapper<ReceiptCode> codeQueryWrapper = new QueryWrapper<>();
                codeQueryWrapper.eq("receipt_code_id", transaction.getReceiptCodeId());
                ReceiptCode receiptCode = receiptCodeMapper.selectOne(codeQueryWrapper);
                
                if (receiptCode != null) {
                    // 验证收款码是否属于当前用户
                    if (!receiptCode.getUserId().equals(userId)) {
                        return Result.instance(ResultEnum.FAILED.getCode(), "无权操作该收款码", null);
                    }
                    
                    // 验证收款码是否过期
                    if (receiptCode.getExpireAt().isBefore(LocalDateTime.now())) {
                        return Result.instance(ResultEnum.FAILED.getCode(), "收款码已过期", null);
                    }
                }
            }
            
            // 标记交易为已确认（这里可以根据实际需求添加更多逻辑）
            // 例如：transaction.setStatus("CONFIRMED");
            // receiptTransactionMapper.updateById(transaction);
            
            // 构建返回结果
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            ReceiptConfirmVo confirmVo = ReceiptConfirmVo.builder()
                    .transactionId(transaction.getTransactionId())
                    .payerId(transaction.getPayerId())
                    .receiverId(transaction.getReceiverId())
                    .receiverName(transaction.getReceiverName())
                    .amount(transaction.getAmount().toString())
                    .timestamp(transaction.getTimestamp().format(formatter))
                    .receiptCodeId(transaction.getReceiptCodeId())
                    .build();
            
            return Result.success("收款成功", confirmVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }

    @Override
    public Result<ReceiptRecordsVo> getReceiptRecords(String authorization) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询该用户的所有收款记录
            QueryWrapper<ReceiptTransaction> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("receiver_id", userId);
            queryWrapper.orderByDesc("timestamp"); // 按交易时间倒序排列
            
            List<ReceiptTransaction> transactions = receiptTransactionMapper.selectList(queryWrapper);
            List<ReceiptRecordVo> recordVos = new ArrayList<>();
            
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            
            // 转换为VO对象
            for (ReceiptTransaction transaction : transactions) {
                ReceiptRecordVo recordVo = ReceiptRecordVo.builder()
                        .transactionId(transaction.getTransactionId())
                        .payerId(transaction.getPayerId())
                        .receiverId(transaction.getReceiverId())
                        .receiverName(transaction.getReceiverName())
                        .amount(transaction.getAmount().toString())
                        .timestamp(transaction.getTimestamp().format(formatter))
                        .build();
                recordVos.add(recordVo);
            }
            
            // 构建返回结果
            ReceiptRecordsVo receiptsVo = ReceiptRecordsVo.builder()
                    .items(recordVos)
                    .build();
            
            return Result.success("查询成功", receiptsVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }

    @Override
    public Result<ReceiptRecordsVo> getRecentReceiptRecords(String authorization, Integer limit) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 设置默认限制为3条记录
            if (limit == null || limit <= 0) {
                limit = 3;
            }
            
            // 查询该用户的最近几条收款记录
            QueryWrapper<ReceiptTransaction> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("receiver_id", userId);
            queryWrapper.orderByDesc("timestamp"); // 按交易时间倒序排列
            queryWrapper.last("LIMIT " + limit); // 限制返回记录数
            
            List<ReceiptTransaction> transactions = receiptTransactionMapper.selectList(queryWrapper);
            List<ReceiptRecordVo> recordVos = new ArrayList<>();
            
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            
            // 转换为VO对象
            for (ReceiptTransaction transaction : transactions) {
                ReceiptRecordVo recordVo = ReceiptRecordVo.builder()
                        .transactionId(transaction.getTransactionId())
                        .payerId(transaction.getPayerId())
                        .receiverId(transaction.getReceiverId())
                        .receiverName(transaction.getReceiverName())
                        .amount(transaction.getAmount().toString())
                        .timestamp(transaction.getTimestamp().format(formatter))
                        .build();
                recordVos.add(recordVo);
            }
            
            // 构建返回结果
            ReceiptRecordsVo receiptsVo = ReceiptRecordsVo.builder()
                    .items(recordVos)
                    .build();
            
            return Result.success("查询成功", receiptsVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
}

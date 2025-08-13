package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.software.code.common.result.Result;
import org.software.code.common.result.ResultEnum;
import org.software.code.common.util.JwtUtil;
import org.software.code.dto.TransitEntryRequestDto;
import org.software.code.dto.TransitExitRequestDto;
import org.software.code.entity.*;
import org.software.code.mapper.*;
import org.software.code.service.TransitService;
import org.software.code.vo.FareCalculationVo;
import org.software.code.vo.TransitEntryResponseVo;
import org.software.code.vo.TransitExitResponseVo;
import org.software.code.vo.TransitRecordVo;
import org.software.code.vo.TransitDetailVo;
import org.software.code.dto.TransitRepayRequestDto;
import org.software.code.vo.TransitRepayResponseVo;
import org.software.code.vo.SiteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出行服务实现类
 */
@Service
public class TransitServiceImpl implements TransitService {


    
    @Autowired
    private TransitRecordMapper transitRecordMapper;
    
    @Autowired
    private SiteMapper siteMapper;
    
    @Autowired
    private TurnstileDeviceMapper turnstileDeviceMapper;
    
    @Autowired
    private SiteFareMapper siteFareMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserBalanceMapper userBalanceMapper;
    
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.S][.SSS]");
    
    @Override
    @Transactional
    public Result<TransitEntryResponseVo> entryStation(String authorization, TransitEntryRequestDto requestDto) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询用户是否有未完成的出行记录
            TransitRecord existingRecord = getUnfinishedTransitRecord(userId);
            
            if (existingRecord != null) {
                // 查询未完成记录的入站站点信息
                Site entrySite = siteMapper.selectById(existingRecord.getEntrySiteId());
                String stationName = entrySite != null ? entrySite.getSiteName() : "未知站点";
                String entryTimeStr = existingRecord.getEntryTime() != null ? 
                        existingRecord.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "未知时间";
                
                // 构建详细的错误信息
                String errorMessage = String.format(
                    "您有未完成的出行记录，请先出站。入站站点：%s，入站时间：%s，交通方式：%s", 
                    stationName, 
                    entryTimeStr,
                    existingRecord.getMode()
                );
                
                // 返回详细的错误信息
                return Result.instance(ResultEnum.FAILED.getCode(), errorMessage, null);
            }
            
            // 查询站点信息
            LambdaQueryWrapper<Site> siteQueryWrapper = Wrappers.<Site>lambdaQuery()
                    .eq(Site::getSiteName, requestDto.getEntryStation());
            Site entrySite = siteMapper.selectOne(siteQueryWrapper);
            
            if (entrySite == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "入站站点不存在", null);
            }
            
            // 解析入站时间
            LocalDateTime entryTime;
            try {
                System.out.println("DEBUG - 解析入站时间: " + requestDto.getEntryTime());
                entryTime = LocalDateTime.parse(requestDto.getEntryTime(), DATETIME_FORMATTER);
                System.out.println("DEBUG - 解析后的入站时间: " + entryTime);
            } catch (DateTimeParseException e) {
                System.out.println("DEBUG - 入站时间解析失败: " + e.getMessage());
                return Result.instance(ResultEnum.FAILED.getCode(), "入站时间格式不正确，请使用yyyy-MM-dd HH:mm:ss格式", null);
            }
            
            // 创建入站记录
            TransitRecord transitRecord = new TransitRecord();
            transitRecord.setUserId(userId);
            transitRecord.setMode(requestDto.getMode());
            transitRecord.setEntrySiteId(entrySite.getId());
            transitRecord.setEntryTime(entryTime);
            transitRecord.setStatus(0); // 正常状态
            
            // 生成交易ID
            String transitId = "T" + System.currentTimeMillis() + (int)(Math.random() * 1000);
            transitRecord.setTransactionId(transitId);
            
            transitRecordMapper.insert(transitRecord);
            
            // 构建响应
            TransitEntryResponseVo responseVo = TransitEntryResponseVo.builder()
                    .transitId(transitId)
                    .mode(requestDto.getMode())
                    .entryStation(requestDto.getEntryStation())
                    .entryTime(entryTime)
                    .entryLine(entrySite.getLine())
                    .userId(userId)
                    .status(0)
                    .build();
            
            return Result.success("进站成功", responseVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    @Override
    @Transactional
    public Result<TransitExitResponseVo> exitStation(String authorization, TransitExitRequestDto requestDto) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询用户的未完成出行记录
            TransitRecord transitRecord = getUnfinishedTransitRecord(userId);
            
            // 查询出站站点信息
            LambdaQueryWrapper<Site> siteQueryWrapper = Wrappers.<Site>lambdaQuery()
                    .eq(Site::getSiteName, requestDto.getExitStation());
            Site exitSite = siteMapper.selectOne(siteQueryWrapper);
            
            if (exitSite == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "出站站点不存在", null);
            }
            
            // 解析出站时间
            LocalDateTime exitTime;
            try {
                System.out.println("DEBUG - 解析出站时间: " + requestDto.getExitTime());
                exitTime = LocalDateTime.parse(requestDto.getExitTime(), DATETIME_FORMATTER);
                System.out.println("DEBUG - 解析后的出站时间: " + exitTime);
            } catch (DateTimeParseException e) {
                System.out.println("DEBUG - 出站时间解析失败: " + e.getMessage());
                return Result.instance(ResultEnum.FAILED.getCode(), "出站时间格式不正确，请使用yyyy-MM-dd HH:mm:ss格式", null);
            }
            
            // 构建响应对象
            TransitExitResponseVo responseVo = new TransitExitResponseVo();
            responseVo.setMode(requestDto.getMode());
            
            // 检查是否有未完成的出行记录
            if (transitRecord == null) {
                responseVo.setTransitId("UNKNOWN");
                responseVo.setStatus(2); // 出行异常
                responseVo.setReason("没有找到未完成的出行记录");
                return Result.instance(ResultEnum.FAILED.getCode(), "出站失败", responseVo);
            }
            
            // 检查出行方式是否匹配
            if (!transitRecord.getMode().equals(requestDto.getMode())) {
                responseVo.setTransitId(transitRecord.getTransactionId());
                responseVo.setStatus(2); // 出行异常
                responseVo.setReason("出行方式不匹配");
                
                // 更新记录状态
                transitRecord.setStatus(2); // 异常状态
                transitRecord.setReason("出行方式不匹配");
                transitRecordMapper.updateById(transitRecord);
                
                return Result.instance(ResultEnum.FAILED.getCode(), "出站失败", responseVo);
            }

            
            if (exitTime.isBefore(transitRecord.getEntryTime())) {
                responseVo.setTransitId(transitRecord.getTransactionId());
                responseVo.setStatus(2); // 出行异常
                responseVo.setReason("出站时间早于进站时间");
                
                // 更新记录状态
                transitRecord.setStatus(2); // 异常状态
                transitRecord.setReason("出站时间早于进站时间");
                transitRecordMapper.updateById(transitRecord);
                
                return Result.instance(ResultEnum.FAILED.getCode(), "出站失败：出站时间早于进站时间", responseVo);
            }
            
            // 检查出站时间是否超过入站时间24小时
            LocalDateTime maxExitTime = transitRecord.getEntryTime().plusHours(24);
            if (exitTime.isAfter(maxExitTime)) {
                responseVo.setTransitId(transitRecord.getTransactionId());
                responseVo.setStatus(2); // 出行异常
                responseVo.setReason("出站时间超过入站时间24小时");
                
                // 更新记录状态
                transitRecord.setStatus(2); // 异常状态
                transitRecord.setReason("出站时间超过入站时间24小时");
                transitRecordMapper.updateById(transitRecord);
                
                return Result.instance(ResultEnum.FAILED.getCode(), "出站失败：超过24小时限制", responseVo);
            }
            
            // 计算持续时间（分钟）
            Duration duration = Duration.between(transitRecord.getEntryTime(), exitTime);
            long durationMinutes = duration.toMinutes();
            
            // 获取入站站点
            Site entrySite = siteMapper.selectById(transitRecord.getEntrySiteId());
            
            // 计算费用
            LambdaQueryWrapper<SiteFare> fareQueryWrapper = Wrappers.<SiteFare>lambdaQuery()
                    .eq(SiteFare::getFromSiteId, transitRecord.getEntrySiteId())
                    .eq(SiteFare::getToSiteId, exitSite.getId())
                    .eq(SiteFare::getTransitType, transitRecord.getMode())
                    .eq(SiteFare::getStatus, "ACTIVE");
            SiteFare siteFare = siteFareMapper.selectOne(fareQueryWrapper);
            
            BigDecimal amount;
            if (siteFare != null) {
                amount = siteFare.getBaseFare();
            } else {
                // 如果没有找到对应的费用记录，使用默认计算方式
                amount = calculateDefaultFare(transitRecord.getEntrySiteId(), exitSite.getId(), transitRecord.getMode());
            }
            
            // 更新出行记录
            transitRecord.setExitSiteId(exitSite.getId());
            transitRecord.setExitTime(exitTime);
            transitRecord.setAmount(amount);
            transitRecord.setActualAmount(amount); // 不考虑折扣
            
            // 生成交易ID
            String transactionId = "TC" + System.currentTimeMillis() + (int)(Math.random() * 1000);
            
            // 检查用户余额
            LambdaQueryWrapper<UserBalance> balanceQueryWrapper = Wrappers.<UserBalance>lambdaQuery()
                    .eq(UserBalance::getUserId, userId);
            UserBalance userBalance = userBalanceMapper.selectOne(balanceQueryWrapper);
            
            if (userBalance == null || userBalance.getBalance().compareTo(amount) < 0) {
                // 余额不足
                responseVo.setTransitId(transitRecord.getTransactionId());
                responseVo.setStatus(1); // 支付异常
                responseVo.setReason("余额不足");
                responseVo.setFee(amount.toString());
                responseVo.setDuration(String.valueOf(durationMinutes));
                
                // 更新记录状态
                transitRecord.setStatus(1); // 支付异常
                transitRecord.setReason("余额不足");
                transitRecordMapper.updateById(transitRecord);
                
                return Result.instance(ResultEnum.FAILED.getCode(), "出站失败：余额不足", responseVo);
            }
            
            // 扣除用户余额
            userBalance.setBalance(userBalance.getBalance().subtract(amount));
            userBalanceMapper.updateById(userBalance);
            
            // 更新出行记录
            transitRecord.setStatus(0); // 正常状态
            transitRecord.setTransactionId(transactionId);
            transitRecordMapper.updateById(transitRecord);
            
            // 构建成功响应
            responseVo.setTransitId(transitRecord.getTransactionId());
            responseVo.setStatus(0); // 出行正常
            responseVo.setFee(amount.toString());
            responseVo.setDuration(String.valueOf(durationMinutes));
            responseVo.setTranscationId(transactionId);
            
            return Result.success("出站成功", responseVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    @Override
    public Result<List<TransitRecordVo>> getUserTransitRecords(String authorization, Integer limit) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 默认查询10条记录
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            
            // 查询用户的出行记录
            List<Map<String, Object>> records = transitRecordMapper.selectUserTransitRecordsWithSites(userId, limit);
            
            // 转换为VO
            List<TransitRecordVo> transitRecordVos = new ArrayList<>();
            for (Map<String, Object> record : records) {
                TransitRecordVo vo = new TransitRecordVo();
                // 设置基本属性（不包含id）
                vo.setUserId(record.get("user_id") != null ? Long.valueOf(record.get("user_id").toString()) : null);
                vo.setMode(record.get("mode") != null ? record.get("mode").toString() : null);
                vo.setEntrySiteId(record.get("entry_site_id") != null ? Long.valueOf(record.get("entry_site_id").toString()) : null);
                vo.setEntrySiteName(record.get("entry_site_name") != null ? record.get("entry_site_name").toString() : null);
                vo.setEntrySiteLine(record.get("entry_site_line") != null ? record.get("entry_site_line").toString() : null);
                
                // 设置可能为空的属性
                if (record.get("exit_site_id") != null) {
                    vo.setExitSiteId(record.get("exit_site_id") != null ? Long.valueOf(record.get("exit_site_id").toString()) : null);
                    vo.setExitSiteName(record.get("exit_site_name") != null ? record.get("exit_site_name").toString() : null);
                    vo.setExitSiteLine(record.get("exit_site_line") != null ? record.get("exit_site_line").toString() : null);
                }
                
                // 解析日期时间
                String entryTimeStr = record.get("entry_time") != null ? record.get("entry_time").toString() : null;
                if (entryTimeStr != null) {
                    vo.setEntryTime(LocalDateTime.parse(entryTimeStr, DATETIME_FORMATTER));
                }
                if (record.get("exit_time") != null) {
                    vo.setExitTime(LocalDateTime.parse(record.get("exit_time").toString(), DATETIME_FORMATTER));
                }
                
                // 设置金额
                if (record.get("amount") != null) {
                    vo.setAmount(new BigDecimal(record.get("amount").toString()));
                }
                if (record.get("actual_amount") != null) {
                    vo.setActualAmount(new BigDecimal(record.get("actual_amount").toString()));
                }
                
                // 设置状态和原因
                vo.setStatus(record.get("status") != null ? Integer.valueOf(record.get("status").toString()) : null);
                
                // 特别处理reason字段，确保它被正确设置
                if (record.get("reason") != null && record.get("reason").toString().trim().length() > 0) {
                    vo.setReason(record.get("reason").toString());
                    System.out.println("DEBUG - 设置了reason: " + record.get("reason").toString());
                } else {
                    System.out.println("DEBUG - reason为空");
                }
                
                // 设置交易ID
                if (record.get("transaction_id") != null) {
                    vo.setTransactionId(record.get("transaction_id").toString());
                }
                
                transitRecordVos.add(vo);
            }
            
            return Result.success("查询成功", transitRecordVos);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }

    @Override
    public Result<TransitDetailVo> getTransitDetail(String authorization, String transitId) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询出行记录
            LambdaQueryWrapper<TransitRecord> queryWrapper = Wrappers.<TransitRecord>lambdaQuery()
                    .eq(TransitRecord::getUserId, userId)
                    .eq(TransitRecord::getTransactionId, transitId);
            TransitRecord record = transitRecordMapper.selectOne(queryWrapper);
            
            if (record == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "未找到出行记录", null);
            }
            
            // 查询入站站点
            Site entrySite = siteMapper.selectById(record.getEntrySiteId());
            if (entrySite == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "站点信息不存在", null);
            }
            
            // 构建详情VO
            TransitDetailVo detailVo = TransitDetailVo.builder()
                    .transitId(record.getTransactionId())
                    .mode(record.getMode())
                    .userId(record.getUserId())
                    .entrySiteId(record.getEntrySiteId())
                    .entrySiteName(entrySite.getSiteName())
                    .entrySiteLine(entrySite.getLine())
                    .entryTime(record.getEntryTime())
                    .status(record.getStatus())
                    .build();
            
            // 如果已出站，添加出站信息
            if (record.getExitSiteId() != null) {
                Site exitSite = siteMapper.selectById(record.getExitSiteId());
                if (exitSite != null) {
                    detailVo.setExitSiteId(record.getExitSiteId());
                    detailVo.setExitSiteName(exitSite.getSiteName());
                    detailVo.setExitSiteLine(exitSite.getLine());
                }
                
                detailVo.setExitTime(record.getExitTime());
                detailVo.setAmount(record.getAmount());
                detailVo.setActualAmount(record.getActualAmount());
                
                // 计算持续时间
                if (record.getEntryTime() != null && record.getExitTime() != null) {
                    Duration duration = Duration.between(record.getEntryTime(), record.getExitTime());
                    detailVo.setDuration(duration.toMinutes());
                }
            }
            
            // 设置其他字段
            detailVo.setReason(record.getReason());
            detailVo.setTransactionId(record.getTransactionId());
            
            return Result.success("查询成功", detailVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    @Override
    @Transactional
    public Result<TransitRepayResponseVo> repayTransit(String authorization, TransitRepayRequestDto requestDto) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询出行记录
            LambdaQueryWrapper<TransitRecord> queryWrapper = Wrappers.<TransitRecord>lambdaQuery()
                    .eq(TransitRecord::getTransactionId, requestDto.getTransitId())
                    .eq(TransitRecord::getUserId, userId);
            TransitRecord record = transitRecordMapper.selectOne(queryWrapper);
            
            if (record == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "未找到出行记录", null);
            }
            
            // 检查记录是否为支付异常状态
            if (record.getStatus() != 1) {
                return Result.instance(ResultEnum.FAILED.getCode(), "此记录不是支付异常状态，不需要补缴", null);
            }
            
            // 解析支付金额
            BigDecimal amount;
            try {
                amount = new BigDecimal(requestDto.getAmount());
            } catch (NumberFormatException e) {
                return Result.instance(ResultEnum.FAILED.getCode(), "金额格式不正确", null);
            }
            
            // 验证金额是否正确
            if (record.getAmount() != null && amount.compareTo(record.getAmount()) != 0) {
                return Result.instance(ResultEnum.FAILED.getCode(), "支付金额与应付金额不一致", null);
            }
            
            // 解析支付时间
            LocalDateTime payTime;
            try {
                payTime = LocalDateTime.parse(requestDto.getPayTime(), DATETIME_FORMATTER);
            } catch (DateTimeParseException e) {
                return Result.instance(ResultEnum.FAILED.getCode(), "支付时间格式不正确，请使用yyyy-MM-dd HH:mm:ss格式", null);
            }
            
            // 检查用户余额
            LambdaQueryWrapper<UserBalance> balanceQueryWrapper = Wrappers.<UserBalance>lambdaQuery()
                    .eq(UserBalance::getUserId, userId);
            UserBalance userBalance = userBalanceMapper.selectOne(balanceQueryWrapper);
            
            if (userBalance == null || userBalance.getBalance().compareTo(amount) < 0) {
                return Result.instance(ResultEnum.FAILED.getCode(), "余额不足，无法补缴", null);
            }
            
            // 扣除用户余额
            userBalance.setBalance(userBalance.getBalance().subtract(amount));
            userBalanceMapper.updateById(userBalance);
            
            // 更新出行记录状态
            record.setStatus(0); // 更新为正常状态
            record.setReason(null); // 清除异常原因
            transitRecordMapper.updateById(record);
            
            // 构建响应
            TransitRepayResponseVo responseVo = TransitRepayResponseVo.builder()
                    .status("SUCCESS")
                    .clearedAt(payTime.format(DATETIME_FORMATTER))
                    .transcationId(requestDto.getTranscationId())
                    .build();
            
            return Result.success("补缴成功", responseVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }

    @Override
    public Result<List<SiteVo>> getCityStations(String authorization, String city, String type) {
        try {
            // Validate the authorization token
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            if (userId == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "无效的Token", null);
            }
            
            // Create a query wrapper to filter by city and optionally by type
            LambdaQueryWrapper<Site> queryWrapper = Wrappers.<Site>lambdaQuery()
                    .eq(Site::getCity, city)
                    .eq(Site::getStatus, "ACTIVE");  // Only return active sites
            
            // Add type filter if provided
            if (type != null && !type.isEmpty()) {
                queryWrapper.eq(Site::getType, type);
            }
            
            // Query sites from database
            List<Site> sites = siteMapper.selectList(queryWrapper);
            
            // Convert entities to view objects
            List<SiteVo> siteVos = sites.stream()
                    .map(site -> SiteVo.builder()
                            .id(site.getId())
                            .siteCode(site.getSiteCode())
                            .siteName(site.getSiteName())
                            .city(site.getCity())
                            .cityCode(site.getCityCode())
                            .line(site.getLine())
                            .longitude(site.getLongitude())
                            .latitude(site.getLatitude())
                            .address(site.getAddress())
                            .type(site.getType())
                            .build())
                    .collect(Collectors.toList());
            
            return Result.success("查询成功", siteVos);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }

    
    /**
     * 获取用户未完成的出行记录（仅状态正常的记录）
     * @param userId 用户ID
     * @return 未完成的出行记录，如果没有则返回null
     */
    private TransitRecord getUnfinishedTransitRecord(Long userId) {
        // 查询用户是否有未完成的出行记录（仅状态为正常的记录）
        LambdaQueryWrapper<TransitRecord> queryWrapper = Wrappers.<TransitRecord>lambdaQuery()
                .eq(TransitRecord::getUserId, userId)
                .eq(TransitRecord::getStatus, 0)  // 只查询状态正常的记录
                .isNull(TransitRecord::getExitSiteId)
                .isNull(TransitRecord::getExitTime)
                .orderByDesc(TransitRecord::getEntryTime)  // 按入站时间倒序排列
                .last("LIMIT 1");  // 只返回最近的一条记录
        return transitRecordMapper.selectOne(queryWrapper);
    }

    /**
     * 默认费用计算方法
     * @param entrySiteId 入站站点ID
     * @param exitSiteId 出站站点ID
     * @param transitType 交通类型
     * @return 计算的费用
     */
    private BigDecimal calculateDefaultFare(Long entrySiteId, Long exitSiteId, String transitType) {
        // 基础票价
        BigDecimal baseFare = new BigDecimal("2.00");
        
        // 获取站点信息
        Site entrySite = siteMapper.selectById(entrySiteId);
        Site exitSite = siteMapper.selectById(exitSiteId);
        
        if (entrySite != null && exitSite != null) {
            // 如果是同一条线路
            if (entrySite.getLine().equals(exitSite.getLine())) {
                try {
                    // 使用Long解析站点编号，避免数字超出Integer范围
                    long entryStationCode = Long.parseLong(entrySite.getSiteCode());
                    long exitStationCode = Long.parseLong(exitSite.getSiteCode());
                    long stationDiff = Math.abs(entryStationCode - exitStationCode);
                    
                    // 假设每站加0.5元，但限制最大站数差为20站
                    long maxStationDiff = Math.min(stationDiff, 20);
                    return baseFare.add(new BigDecimal("0.5").multiply(new BigDecimal(maxStationDiff)));
                } catch (NumberFormatException e) {
                    // 如果站点编号无法解析为数字，返回基础票价
                    return baseFare;
                }
            } else {
                // 不同线路，额外加2元换乘费
                return baseFare.add(new BigDecimal("2.00"));
            }
        }
        
        // 默认返回基础票价
        return baseFare;
    }
} 
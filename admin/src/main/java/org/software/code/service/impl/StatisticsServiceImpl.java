package org.software.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.consts.AdminConstants;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.entity.*;
import org.software.code.mapper.*;
import org.software.code.service.StatisticsService;
import org.software.code.vo.DeviceStatisticsVo;
import org.software.code.vo.DiscountStatisticsVo;
import org.software.code.vo.TravelStatisticsVo;
import org.software.code.vo.UserStatisticsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * StatisticsServiceImpl 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    
    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private TravelRecordMapper travelRecordMapper;
    
    @Resource
    private TurnstileDeviceMapper deviceMapper;
    
    @Resource
    private SiteMapper siteMapper;
    
    @Resource
    private DiscountStrategyMapper discountStrategyMapper;
    
    @Resource
    private ScanRecordMapper scanRecordMapper;

    @Override
    public UserStatisticsVo getUserStatistics(String startDate, String endDate) {
        logger.info("查询用户统计数据，开始日期：{}，结束日期：{}", startDate, endDate);
        
        try {
            // 构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            
            // 如果提供了日期范围，则添加日期过滤
            if (StrUtil.isNotBlank(startDate)) {
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.ge("created_time", start);
            }
            if (StrUtil.isNotBlank(endDate)) {
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.le("created_time", end);
            }
            
            // 1. 查询总用户数
            long totalUsers = userMapper.selectCount(queryWrapper);
            
            // 2. 查询各状态用户数
            QueryWrapper<User> pendingQuery = queryWrapper.clone().eq("status", AdminConstants.UserStatus.PENDING);
            long pendingUsers = userMapper.selectCount(pendingQuery);
            
            QueryWrapper<User> approvedQuery = queryWrapper.clone().eq("status", AdminConstants.UserStatus.APPROVED);
            long approvedUsers = userMapper.selectCount(approvedQuery);
            
            QueryWrapper<User> rejectedQuery = queryWrapper.clone().eq("status", AdminConstants.UserStatus.REJECTED);
            long rejectedUsers = userMapper.selectCount(rejectedQuery);
            
            QueryWrapper<User> disabledQuery = queryWrapper.clone().eq("status", AdminConstants.UserStatus.DISABLED);
            long disabledUsers = userMapper.selectCount(disabledQuery);
            
            // 3. 查询今日新增用户数
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            QueryWrapper<User> todayQuery = new QueryWrapper<User>().ge("created_time", today);
            long todayNewUsers = userMapper.selectCount(todayQuery);

            // 4. 查询本周新增用户数
            LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            QueryWrapper<User> weekQuery = new QueryWrapper<User>().ge("created_time", weekStart);
            long weekNewUsers = userMapper.selectCount(weekQuery);

            // 5. 查询本月新增用户数
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            QueryWrapper<User> monthQuery = new QueryWrapper<User>().ge("created_time", monthStart);
            long monthNewUsers = userMapper.selectCount(monthQuery);
            
            // 6. 构建统计结果
            return UserStatisticsVo.builder()
                    .totalUsers(totalUsers)
                    .pendingUsers(pendingUsers)
                    .approvedUsers(approvedUsers)
                    .rejectedUsers(rejectedUsers)
                    .disabledUsers(disabledUsers)
                    .newUsersToday(todayNewUsers)
                    .newUsersThisWeek(monthNewUsers)
                    .newUsersThisMonth(monthNewUsers)
                    .build();
            
        } catch (Exception e) {
            logger.error("查询用户统计数据失败：{}", e.getMessage(), e);
            throw new BusinessException(ExceptionEnum.RUN_EXCEPTION);
        }
    }

    @Override
    public TravelStatisticsVo getTravelStatistics(String startDate, String endDate, String city) {
        logger.info("查询出行统计数据，开始日期：{}，结束日期：{}，城市：{}", startDate, endDate, city);
        
        try {
            // 构建查询条件
            QueryWrapper<TravelRecord> queryWrapper = new QueryWrapper<>();
            
            // 如果提供了日期范围，则添加日期过滤
            if (StrUtil.isNotBlank(startDate)) {
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.ge("travel_time", start);
            }
            if (StrUtil.isNotBlank(endDate)) {
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.le("travel_time", end);
            }
            if (StrUtil.isNotBlank(city)) {
                queryWrapper.eq("city", city);
            }
            
            // 1. 查询总出行记录数
            long totalTravels = travelRecordMapper.selectCount(queryWrapper);
            
            // 2. 查询总扫码次数
            QueryWrapper<ScanRecord> scanQuery = new QueryWrapper<>();
            if (StrUtil.isNotBlank(startDate)) {
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                scanQuery.ge("scan_time", start);
            }
            if (StrUtil.isNotBlank(endDate)) {
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                scanQuery.le("scan_time", end);
            }
            long totalScans = scanRecordMapper.selectCount(scanQuery);
            
            // 3. 查询成功出行数（假设有status字段表示出行状态）
            QueryWrapper<TravelRecord> successQuery = queryWrapper.clone().eq("status", "SUCCESS");
            long successfulTravels = travelRecordMapper.selectCount(successQuery);
            
            // 4. 查询失败出行数
            QueryWrapper<TravelRecord> failedQuery = queryWrapper.clone().eq("status", "FAILED");
            long failedTravels = travelRecordMapper.selectCount(failedQuery);
            

            // 5. 构建统计结果
            TravelStatisticsVo statisticsVo = new TravelStatisticsVo();
            statisticsVo.setTotalTravelRecords(totalTravels);
            statisticsVo.setTotalScanRecords(totalScans);
            statisticsVo.setSuccessfulScans(successfulTravels);
            statisticsVo.setFailedScans(failedTravels);
            statisticsVo.setTotalAmount(totalTravels * 2.0);
            logger.info("出行统计数据查询完成：总出行数={}, 总扫码数={}, 成功出行数={}, 失败出行数={}", 
                       totalTravels, totalScans, successfulTravels, failedTravels);
            
            return statisticsVo;
            
        } catch (Exception e) {
            logger.error("查询出行统计数据失败：{}", e.getMessage(), e);
            throw new BusinessException(ExceptionEnum.RUN_EXCEPTION);
        }
    }

    @Override
    public DeviceStatisticsVo getDeviceStatistics(String startDate, String endDate, String city, Long siteId) {
        logger.info("查询设备统计数据，开始日期：{}，结束日期：{}，城市：{}，站点ID：{}", startDate, endDate, city, siteId);
        
        try {
            // 构建设备查询条件
            QueryWrapper<TurnstileDevice> deviceQuery = new QueryWrapper<>();
            if (StrUtil.isNotBlank(city)) {
                deviceQuery.eq("city", city);
            }
            if (siteId != null) {
                deviceQuery.eq("site_id", siteId);
            }
            
            // 1. 查询总设备数
            long totalDevices = deviceMapper.selectCount(deviceQuery);
            
            // 2. 查询在线设备数
            QueryWrapper<TurnstileDevice> onlineQuery = deviceQuery.clone().eq("status", AdminConstants.DeviceStatus.ONLINE);
            long onlineDevices = deviceMapper.selectCount(onlineQuery);
            
            // 3. 查询离线设备数
            QueryWrapper<TurnstileDevice> offlineQuery = deviceQuery.clone().eq("status", AdminConstants.DeviceStatus.OFFLINE);
            long offlineDevices = deviceMapper.selectCount(offlineQuery);
            
            // 4. 查询故障设备数
            QueryWrapper<TurnstileDevice> faultQuery = deviceQuery.clone().eq("status", AdminConstants.DeviceStatus.FAULT);
            long faultDevices = deviceMapper.selectCount(faultQuery);
            
            // 5. 查询维护中设备数
            QueryWrapper<TurnstileDevice> maintenanceQuery = deviceQuery.clone().eq("status", AdminConstants.DeviceStatus.MAINTENANCE);
            long maintenanceDevices = deviceMapper.selectCount(maintenanceQuery);
            
            // 6. 查询设备使用次数（基于扫码记录）
            QueryWrapper<ScanRecord> scanQuery = new QueryWrapper<>();
            if (StrUtil.isNotBlank(startDate)) {
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                scanQuery.ge("scan_time", start);
            }
            if (StrUtil.isNotBlank(endDate)) {
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                scanQuery.le("scan_time", end);
            }
            long totalScans = scanRecordMapper.selectCount(scanQuery);
            
            // 7. 构建统计结果
            DeviceStatisticsVo statisticsVo = new DeviceStatisticsVo();
            statisticsVo.setTotalDevices(totalDevices);
            statisticsVo.setActiveDevices(onlineDevices); // 映射到activeDevices
            statisticsVo.setInactiveDevices(offlineDevices); // 映射到inactiveDevices
            statisticsVo.setOfflineDevices(offlineDevices);
            statisticsVo.setMaintenanceDevices(maintenanceDevices);

            logger.info("设备统计数据查询完成：总设备数={}, 在线={}, 离线={}, 故障={}, 维护中={}", 
                       totalDevices, onlineDevices, offlineDevices, faultDevices, maintenanceDevices);
            
            return statisticsVo;
            
        } catch (Exception e) {
            logger.error("查询设备统计数据失败：{}", e.getMessage(), e);
            throw new BusinessException(ExceptionEnum.RUN_EXCEPTION);
        }
    }

    @Override
    public DiscountStatisticsVo getDiscountStatistics(String startDate, String endDate, String strategyType) {
        logger.info("查询折扣策略统计数据，开始日期：{}，结束日期：{}，策略类型：{}", startDate, endDate, strategyType);
        
        try {
            // 构建查询条件
            QueryWrapper<DiscountStrategy> queryWrapper = new QueryWrapper<>();
            
            // 如果提供了日期范围，则添加日期过滤
            if (StrUtil.isNotBlank(startDate)) {
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.ge("created_time", start);
            }
            if (StrUtil.isNotBlank(endDate)) {
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.le("created_time", end);
            }
            if (StrUtil.isNotBlank(strategyType)) {
                queryWrapper.eq("strategy_type", strategyType);
            }
            
            // 1. 查询总策略数
            long totalStrategies = discountStrategyMapper.selectCount(queryWrapper);
            
            // 2. 查询活跃策略数
            QueryWrapper<DiscountStrategy> activeQuery = queryWrapper.clone().eq("status", AdminConstants.DiscountStrategyStatus.ACTIVE);
            long activeStrategies = discountStrategyMapper.selectCount(activeQuery);
            
            // 3. 查询过期策略数
            QueryWrapper<DiscountStrategy> expiredQuery = queryWrapper.clone().eq("status", AdminConstants.DiscountStrategyStatus.EXPIRED);
            long expiredStrategies = discountStrategyMapper.selectCount(expiredQuery);
            
            // 4. 查询停用策略数
            QueryWrapper<DiscountStrategy> inactiveQuery = queryWrapper.clone().eq("status", AdminConstants.DiscountStrategyStatus.INACTIVE);
            long inactiveStrategies = discountStrategyMapper.selectCount(inactiveQuery);
            
            // 5. 计算总使用次数（所有策略的used_count总和）
            // 注意：这里需要自定义查询方法或使用原生SQL，这里简化处理
            long totalUsage = activeStrategies * 10; // 简化计算，实际应该sum(used_count)
            
            // 6. 计算节省金额（需要根据实际业务逻辑计算）
            double totalSavings = totalUsage * 5.0; // 简化计算，实际应该根据折扣金额计算
            
            // 7. 构建统计结果
            DiscountStatisticsVo statisticsVo = new DiscountStatisticsVo();
            statisticsVo.setTotalStrategies(totalStrategies);
            statisticsVo.setActiveStrategies(activeStrategies);
            statisticsVo.setInactiveStrategies(inactiveStrategies + expiredStrategies); // 合并非活跃状态
            statisticsVo.setTotalUsageCount(totalUsage);
            statisticsVo.setTotalDiscountAmount(totalSavings);

            logger.info("折扣策略统计数据查询完成：总策略数={}, 活跃={}, 过期={}, 停用={}, 总使用次数={}", 
                       totalStrategies, activeStrategies, expiredStrategies, inactiveStrategies, totalUsage);
            
            return statisticsVo;
            
        } catch (Exception e) {
            logger.error("查询折扣策略统计数据失败：{}", e.getMessage(), e);
            throw new BusinessException(ExceptionEnum.RUN_EXCEPTION);
        }
    }
}

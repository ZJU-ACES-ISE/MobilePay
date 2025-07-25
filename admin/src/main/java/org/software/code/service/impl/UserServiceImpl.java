package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.consts.AdminConstants;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.dto.UserAuditDto;
import org.software.code.dto.UserSearchDto;
import org.software.code.entity.Site;
import org.software.code.entity.TravelRecord;
import org.software.code.entity.User;
import org.software.code.entity.UserAuditRecord;
import org.software.code.mapper.SiteMapper;
import org.software.code.mapper.TravelRecordMapper;
import org.software.code.mapper.UserAuditRecordMapper;
import org.software.code.mapper.UserMapper;
import org.software.code.service.UserService;
import org.software.code.vo.TravelRecordVo;
import org.software.code.vo.UserDetailVo;
import org.software.code.vo.UserListVo;
import org.software.code.vo.UserStatisticsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAuditRecordMapper userAuditRecordMapper;

    @Resource
    private TravelRecordMapper travelRecordMapper;

    @Resource
    private SiteMapper siteMapper;

    @Override
    public Page<UserListVo> getUserPage(Integer pageNum, Integer pageSize, UserSearchDto searchDto) {
        logger.info("获取用户列表分页数据，页码：{}，页大小：{}", pageNum, pageSize);

        Page<User> userPage = new Page<>(pageNum, pageSize);
        Page<User> resultPage = userMapper.selectPage(
            userPage,
            Wrappers.<User>lambdaQuery()
                .like(StringUtils.hasText(searchDto.getKeyword()), User::getUsername, searchDto.getKeyword())
                .eq(StringUtils.hasText(searchDto.getStatus()), User::getStatus, searchDto.getStatus())
        );

        // 直接使用 convert 转换 records
        return (Page<UserListVo>) resultPage.convert(this::convertToUserListVo);
    }

    @Override
    public UserDetailVo getUserDetail(Long userId) {
        logger.info("获取用户详细信息，用户ID：{}", userId);

        org.software.code.entity.User user = userMapper.selectById(userId);
        if (user == null) {
            logger.warn("用户不存在，用户ID：{}", userId);
            return null;
        }

        UserDetailVo userDetailVo = convertToUserDetailVo(user);

        // 查询审核记录
        List<UserAuditRecord> auditRecords = userAuditRecordMapper.selectList(new QueryWrapper<UserAuditRecord>().eq("user_id", userId));
        if (!CollectionUtils.isEmpty(auditRecords)) {
            List<UserDetailVo.AuditRecordVo> auditRecordVos = auditRecords.stream()
                    .map(this::convertToAuditRecordVo)
                    .collect(Collectors.toList());
            userDetailVo.setAuditRecords(auditRecordVos);
        }

        return userDetailVo;
    }

    @Override
    @Transactional
    public Boolean auditUser(Long userId, UserAuditDto auditDto, Long adminId) {
        logger.info("审核用户，用户ID：{}，审核结果：{}", userId, auditDto.getAuditResult());

        try {
            org.software.code.entity.User user = userMapper.selectById(userId);
            if (user == null) {
                logger.warn("用户不存在，用户ID：{}", userId);
                return false;
            }

            // 更新用户状态
            String newStatus = AdminConstants.AuditResult.APPROVED.equals(auditDto.getAuditResult())
                    ? AdminConstants.UserStatus.APPROVED
                    : AdminConstants.UserStatus.REJECTED;

            user.setStatus(newStatus);
            userMapper.updateById(user);

            // 记录审核日志
            UserAuditRecord auditRecord = new UserAuditRecord();
            auditRecord.setUserId(userId);
            auditRecord.setAdminId(adminId);
            auditRecord.setAuditType(auditDto.getAuditType());
            auditRecord.setAuditResult(auditDto.getAuditResult());
            auditRecord.setAuditReason(auditDto.getAuditReason());
            userAuditRecordMapper.insert(auditRecord);

            logger.info("用户审核成功，用户ID：{}，新状态：{}", userId, newStatus);
            return true;

        } catch (Exception e) {
            logger.error("用户审核失败，用户ID：{}，错误：{}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean batchUpdateUserStatus(List<Long> userIds, String status, Long adminId) {
        logger.info("批量更新用户状态，用户数量：{}，新状态：{}", userIds.size(), status);

        try {
            int updateCount = userMapper.update(null, Wrappers.<User>lambdaUpdate()
                    .set(User::getStatus, status)
                    .in(User::getId, userIds));
            logger.info("批量更新用户状态成功，影响行数：{}", updateCount);
            return updateCount > 0;

        } catch (Exception e) {
            logger.error("批量更新用户状态失败，错误：{}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean enableUser(Long userId, Long adminId) {
        logger.info("启用用户，用户ID：{}", userId);
        return updateUserStatus(userId, AdminConstants.UserStatus.APPROVED, adminId);
    }

    @Override
    public Boolean disableUser(Long userId, Long adminId, String reason) {
        logger.info("禁用用户，用户ID：{}，原因：{}", userId, reason);

        try {
            org.software.code.entity.User user = userMapper.selectById(userId);
            if (user == null) {
                return false;
            }

            // 更新用户状态为禁用
            user.setStatus(AdminConstants.UserStatus.DISABLED);
            userMapper.updateById(user);

            // 记录审核日志
            UserAuditRecord auditRecord = new UserAuditRecord();
            auditRecord.setUserId(userId);
            auditRecord.setAdminId(adminId);
            auditRecord.setAuditType("DISABLE");
            auditRecord.setAuditResult("DISABLED");
            auditRecord.setAuditReason(reason);
            userAuditRecordMapper.insert(auditRecord);

            return true;

        } catch (Exception e) {
            logger.error("禁用用户失败，用户ID：{}，错误：{}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<UserListVo> searchUsersByKeyword(String keyword) {
        logger.info("根据关键字搜索用户，关键字：{}", keyword);

        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery()
                .like(User::getUsername, keyword));
        users.addAll(userMapper.selectList(Wrappers.<User>lambdaQuery()
                .like(User::getRealName, keyword)));
        return users.stream()
                .map(this::convertToUserListVo)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserListVo> getPendingUsers(Integer pageNum, Integer pageSize) {
        logger.info("获取待审核用户列表，页码：{}，页大小：{}", pageNum, pageSize);

        Page<User> resultPage = userMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<User>lambdaQuery()
                        .eq(User::getStatus, AdminConstants.UserStatus.PENDING)
        );

        Page<UserListVo> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");

        List<UserListVo> userListVos = resultPage.getRecords().stream()
                .map(this::convertToUserListVo)
                .collect(Collectors.toList());

        voPage.setRecords(userListVos);
        return voPage;
    }

    @Override
    public UserStatisticsVo getUserStatistics() {
        logger.info("查询用户统计数据");
        
        try {
            // 1. 查询总用户数
            long totalUsers = userMapper.selectCount(null);
            
            // 2. 查询各状态用户数
            long pendingUsers = userMapper.selectCount(
                new QueryWrapper<User>().eq("status", AdminConstants.UserStatus.PENDING)
            );
            long approvedUsers = userMapper.selectCount(
                new QueryWrapper<User>().eq("status", AdminConstants.UserStatus.APPROVED)
            );
            long rejectedUsers = userMapper.selectCount(
                new QueryWrapper<User>().eq("status", AdminConstants.UserStatus.REJECTED)
            );
            long disabledUsers = userMapper.selectCount(
                new QueryWrapper<User>().eq("status", AdminConstants.UserStatus.DISABLED)
            );
            
            // 3. 查询今日新增用户数
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            long todayNewUsers = userMapper.selectCount(
                new QueryWrapper<User>().ge("created_time", today)
            );
            
            // 4. 查询本月新增用户数
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            long monthNewUsers = userMapper.selectCount(
                new QueryWrapper<User>().ge("created_time", monthStart)
            );
            
            // 5. 构建统计结果
            UserStatisticsVo statisticsVo = UserStatisticsVo.builder()
                    .totalUsers(totalUsers)
                    .pendingUsers(pendingUsers)
                    .approvedUsers(approvedUsers)
                    .rejectedUsers(rejectedUsers)
                    .disabledUsers(disabledUsers)
                    .build();
            
            logger.info("用户统计数据查询完成：总用户数={}, 待审核={}, 已通过={}, 已拒绝={}, 已禁用={}", 
                       totalUsers, pendingUsers, approvedUsers, rejectedUsers, disabledUsers);
            
            return statisticsVo;
            
        } catch (Exception e) {
            logger.error("查询用户统计数据失败：{}", e.getMessage(), e);
            throw new BusinessException(ExceptionEnum.RUN_EXCEPTION);
        }
    }


    @Override
    @Transactional
    public Boolean approveUser(Long userId, UserAuditDto auditDto, Long adminId) {
        logger.info("审核通过用户，用户ID：{}，管理员ID：{}", userId, adminId);

        // 设置审核结果为通过
        auditDto.setAuditResult(AdminConstants.AuditResult.APPROVED);

        return auditUser(userId, auditDto, adminId);
    }

    @Override
    public Page<TravelRecordVo> getUserTravelRecords(Long userId, Integer pageNum, Integer pageSize) {
        logger.info("获取用户出行记录，用户ID：{}，页码：{}，页大小：{}", userId, pageNum, pageSize);

        // 使用MyBatis Plus Lambda查询用户出行记录
        Page<TravelRecord> travelRecordPage = new Page<>(pageNum, pageSize);
        Page<TravelRecord> resultPage = travelRecordMapper.selectPage(
            travelRecordPage,
            Wrappers.<TravelRecord>lambdaQuery()
                .eq(TravelRecord::getUserId, userId)
                .orderByDesc(TravelRecord::getCreatedTime)
        );

        // 转换为VO对象
        Page<TravelRecordVo> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");
        
        List<TravelRecordVo> travelRecordVos = resultPage.getRecords().stream()
                .map(this::convertToTravelRecordVo)
                .collect(Collectors.toList());
        
        voPage.setRecords(travelRecordVos);
        return voPage;
    }

    @Override
    @Transactional
    public Boolean rejectUser(Long userId, UserAuditDto auditDto, Long adminId) {
        logger.info("审核拒绝用户，用户ID：{}，管理员ID：{}", userId, adminId);

        // 设置审核结果为拒绝
        auditDto.setAuditResult(AdminConstants.AuditResult.REJECTED);

        return auditUser(userId, auditDto, adminId);
    }



    private Boolean updateUserStatus(Long userId, String status, Long adminId) {
        try {
            org.software.code.entity.User user = userMapper.selectById(userId);
            if (user == null) {
                return false;
            }

            user.setStatus(status);
            userMapper.updateById(user);

            return true;

        } catch (Exception e) {
            logger.error("更新用户状态失败，用户ID：{}，错误：{}", userId, e.getMessage(), e);
            return false;
        }
    }

    private UserListVo convertToUserListVo(User user) {
        return UserListVo.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .balance(user.getBalance())
                .build();
    }

    private UserDetailVo convertToUserDetailVo(User user) {
        return UserDetailVo.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .username(user.getUsername())
                .realName(user.getRealName())
                .idCard(user.getIdCard())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .balance(user.getBalance())
                .auditRecords(new ArrayList<>())
                .build();
    }

    private UserDetailVo.AuditRecordVo convertToAuditRecordVo(UserAuditRecord auditRecord) {
        return UserDetailVo.AuditRecordVo.builder()
                .auditId(auditRecord.getId())
                .auditType(auditRecord.getAuditType())
                .auditResult(auditRecord.getAuditResult())
                .auditReason(auditRecord.getAuditReason())
                .build();
    }

    private TravelRecordVo convertToTravelRecordVo(TravelRecord travelRecord) {
        // 查询进站和出站站点名称
        String entrySiteName = null;
        String exitSiteName = null;
        
        if (travelRecord.getEntrySiteId() != null) {
            Site entrySite = siteMapper.selectById(travelRecord.getEntrySiteId());
            entrySiteName = entrySite != null ? entrySite.getSiteName() : null;
        }
        
        if (travelRecord.getExitSiteId() != null) {
            Site exitSite = siteMapper.selectById(travelRecord.getExitSiteId());
            exitSiteName = exitSite != null ? exitSite.getSiteName() : null;
        }
        
        // 计算出行时长（分钟）
        Long durationMinutes = null;
        if (travelRecord.getEntryTime() != null && travelRecord.getExitTime() != null) {
            long diffInMillis = travelRecord.getExitTime().getTime() - travelRecord.getEntryTime().getTime();
            durationMinutes = diffInMillis / (1000 * 60); // 转换为分钟
        }
        
        // 状态名称映射
        String statusName = getStatusName(travelRecord.getStatus());
        
        // 支付方式名称映射
        String paymentMethodName = getPaymentMethodName(travelRecord.getPaymentMethod());
        
        return TravelRecordVo.builder()
                .id(travelRecord.getId())
                .userId(travelRecord.getUserId())
                .entrySiteId(travelRecord.getEntrySiteId())
                .exitSiteId(travelRecord.getExitSiteId())
                .entrySiteName(entrySiteName)
                .exitSiteName(exitSiteName)
                .entryDeviceId(travelRecord.getEntryDeviceId())
                .exitDeviceId(travelRecord.getExitDeviceId())
                .entryTime(travelRecord.getEntryTime() != null ? 
                    travelRecord.getEntryTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)
                .exitTime(travelRecord.getExitTime() != null ? 
                    travelRecord.getExitTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)
                .amount(travelRecord.getAmount())
                .discountAmount(travelRecord.getDiscountAmount())
                .actualAmount(travelRecord.getActualAmount())
                .status(travelRecord.getStatus())
                .statusName(statusName)
                .paymentMethod(travelRecord.getPaymentMethod())
                .paymentMethodName(paymentMethodName)
                .durationMinutes(durationMinutes)
                .createdTime(travelRecord.getCreatedTime() != null ? 
                    travelRecord.getCreatedTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)
                .updatedTime(travelRecord.getUpdatedTime() != null ? 
                    travelRecord.getUpdatedTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)
                .build();
    }
    
    private String getStatusName(String status) {
        if (status == null) return null;
        switch (status) {
            case "ENTRY": return "已进站";
            case "EXIT": return "已出站";
            case "COMPLETED": return "行程完成";
            case "CANCELLED": return "行程取消";
            default: return status;
        }
    }
    
    private String getPaymentMethodName(String paymentMethod) {
        if (paymentMethod == null) return null;
        switch (paymentMethod) {
            case "BALANCE": return "余额支付";
            case "ALIPAY": return "支付宝";
            case "WECHAT": return "微信支付";
            case "BANK_CARD": return "银行卡";
            default: return paymentMethod;
        }
    }
}

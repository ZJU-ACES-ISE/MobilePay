package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.software.code.entity.TransitRecord;
import org.software.code.entity.User;
import org.software.code.entity.UserAuditRecord;
import org.software.code.entity.UserBalance;
import org.software.code.entity.TurnstileDevice;
import org.software.code.entity.UserVerification;
import org.software.code.mapper.SiteMapper;
import org.software.code.mapper.TransitRecordMapper;
import org.software.code.mapper.UserAuditRecordMapper;
import org.software.code.mapper.UserBalanceMapper;
import org.software.code.mapper.UserMapper;
import org.software.code.mapper.TurnstileDeviceMapper;
import org.software.code.mapper.UserVerificationMapper;
import org.software.code.service.UserService;
import org.software.code.vo.PendingUserVo;
import org.software.code.vo.TransitRecordVo;
import org.software.code.vo.UserDetailVo;
import org.software.code.vo.UserListVo;
import org.software.code.vo.UserStatisticsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

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
    private UserBalanceMapper userBalanceMapper;

    @Resource
    private UserVerificationMapper userVerificationMapper;

    @Resource
    private TransitRecordMapper transitRecordMapper;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private TurnstileDeviceMapper turnstileDeviceMapper;

    @Override
    public Page<UserListVo> getUserPage(Integer pageNum, Integer pageSize, UserSearchDto searchDto) {
        logger.info("获取用户列表分页数据，页码：{}，页大小：{}", pageNum, pageSize);

        Page<User> userPage = new Page<>(pageNum, pageSize);
        Page<User> resultPage = userMapper.selectPage(
            userPage,
            Wrappers.<User>lambdaQuery()
                .like(StringUtils.hasText(searchDto.getKeyword()), User::getNickname, searchDto.getKeyword())
                .eq(StringUtils.hasText(searchDto.getStatus()), User::getStatus, searchDto.getStatus())
        );

        // 直接使用 convert 转换 records
        return (Page<UserListVo>) resultPage.convert(this::convertToUserListVo);
    }

    @Override
    public UserDetailVo getUserDetail(Long userId) {
        logger.info("获取用户详细信息，用户ID：{}", userId);

        User user = userMapper.selectById(userId);
        if (user == null) {
            logger.warn("用户不存在，用户ID：{}", userId);
            return null;
        }

        UserDetailVo userDetailVo = convertToUserDetailVo(user);

        // 查询审核记录
        List<UserAuditRecord> auditRecords = userAuditRecordMapper.selectList(
            Wrappers.<UserAuditRecord>lambdaQuery().eq(UserAuditRecord::getUserId, userId)
        );
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
                .like(User::getNickname, keyword));
        users.addAll(userMapper.selectList(Wrappers.<User>lambdaQuery()
                .like(User::getPhone, keyword)));
        return users.stream()
                .map(this::convertToUserListVo)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PendingUserVo> getPendingUsers(Integer pageNum, Integer pageSize) {
        logger.info("获取待审核用户列表，页码：{}，页大小：{}", pageNum, pageSize);

        try {
            // 1. 查询status='pending'的user_verification记录
            Page<UserVerification> verificationPage = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<UserVerification> wrapper = Wrappers.<UserVerification>lambdaQuery();
            wrapper.eq(UserVerification::getStatus, "pending");
            wrapper.orderByDesc(UserVerification::getSubmitTime);
            
            Page<UserVerification> resultPage = userVerificationMapper.selectPage(verificationPage, wrapper);
            
            // 2. 关联查询对应的user信息，构建PendingUserVo
            Page<PendingUserVo> voPage = new Page<>();
            BeanUtils.copyProperties(resultPage, voPage, "records");
            
            List<PendingUserVo> pendingUsers = resultPage.getRecords().stream()
                    .map(this::convertToPendingUserVo)
                    .collect(Collectors.toList());
            
            voPage.setRecords(pendingUsers);
            
            logger.info("获取待审核用户列表完成，共查询到{}条记录", pendingUsers.size());
            return voPage;
            
        } catch (Exception e) {
            logger.error("获取待审核用户列表失败：{}", e.getMessage(), e);
            throw new BusinessException(ExceptionEnum.RUN_EXCEPTION);
        }
    }

    @Override
    public UserStatisticsVo getUserStatistics() {
        logger.info("查询用户统计数据");
        
        try {
            // 1. 查询总用户数
            long totalUsers = userMapper.selectCount(null);
            
            // 2. 查询各状态用户数
            long pendingUsers = userMapper.selectCount(
                Wrappers.<User>lambdaQuery().eq(User::getStatus, AdminConstants.UserStatus.PENDING)
            );
            long approvedUsers = userMapper.selectCount(
                Wrappers.<User>lambdaQuery().eq(User::getStatus, AdminConstants.UserStatus.APPROVED)
            );
            long rejectedUsers = userMapper.selectCount(
                Wrappers.<User>lambdaQuery().eq(User::getStatus, AdminConstants.UserStatus.REJECTED)
            );
            long disabledUsers = userMapper.selectCount(
                Wrappers.<User>lambdaQuery().eq(User::getStatus, AdminConstants.UserStatus.DISABLED)
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
                    .newUsersToday(todayNewUsers)
                    .newUsersThisMonth(monthNewUsers)
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
    @Transactional
    public Boolean rejectUser(Long userId, UserAuditDto auditDto, Long adminId) {
        logger.info("审核拒绝用户，用户ID：{}，管理员ID：{}", userId, adminId);

        // 设置审核结果为拒绝
        auditDto.setAuditResult(AdminConstants.AuditResult.REJECTED);

        return auditUser(userId, auditDto, adminId);
    }

    @Override
    public Page<TransitRecordVo> getUserTravelRecords(Long userId, Integer pageNum, Integer pageSize) {
        logger.info("获取用户出行记录，用户ID：{}，页码：{}，页大小：{}", userId, pageNum, pageSize);

        Page<TransitRecord> transitRecordPage = new Page<>(pageNum, pageSize);
        Page<TransitRecord> resultPage = transitRecordMapper.selectPage(
            transitRecordPage,
            Wrappers.<TransitRecord>lambdaQuery()
                .eq(TransitRecord::getUserId, userId)
                .orderByDesc(TransitRecord::getCreatedTime)
        );

        Page<TransitRecordVo> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");
        
        List<TransitRecordVo> transitRecordVos = resultPage.getRecords().stream()
                .map(this::convertToTransitRecordVo)
                .collect(Collectors.toList());
        
        voPage.setRecords(transitRecordVos);
        return voPage;
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
        // 查询用户身份验证信息
        UserVerification verification = userVerificationMapper.selectOne(
            Wrappers.<UserVerification>lambdaQuery().eq(UserVerification::getUserId, user.getId())
        );
        
        // 查询用户余额信息
        UserBalance balance = userBalanceMapper.selectOne(
            Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserId, user.getId())
        );
        
        return UserListVo.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .realName(verification != null ? verification.getRealName() : null)
                .idCard(verification != null ? verification.getIdCard() : null)
                .avatar(user.getAvatarUrl())
                .status(user.getStatus())
                .balance(balance != null ? balance.getBalance() : null)
                .createdTime(user.getCreateTime())
                .updatedTime(user.getUpdateTime())
                .build();
    }

    private UserDetailVo convertToUserDetailVo(User user) {
        // 查询用户身份验证信息
        UserVerification verification = userVerificationMapper.selectOne(
            Wrappers.<UserVerification>lambdaQuery().eq(UserVerification::getUserId, user.getId())
        );
        
        // 查询用户余额信息
        UserBalance balance = userBalanceMapper.selectOne(
            Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserId, user.getId())
        );
        
        return UserDetailVo.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .realName(verification != null ? verification.getRealName() : null)
                .idCard(verification != null ? verification.getIdCard() : null)
                .avatar(user.getAvatarUrl())
                .status(user.getStatus())
                .balance(balance != null ? balance.getBalance() : null)
                .createdTime(user.getCreateTime())
                .updatedTime(user.getUpdateTime())
                .auditRecords(new ArrayList<>())
                .build();
    }

    private PendingUserVo convertToPendingUserVo(UserVerification verification) {
        // 查询对应的user信息
        User user = userMapper.selectById(verification.getUserId());
        if (user == null) {
            logger.warn("用户不存在，user_id: {}", verification.getUserId());
            return null;
        }
        
        return PendingUserVo.builder()
                // 用户基本信息
                .userId(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                // 身份验证信息
                .verificationId(verification.getId())
                .realName(verification.getRealName())
                .idCard(verification.getIdCard())
                .idCardFront(verification.getIdCardFront())
                .idCardBack(verification.getIdCardBack())
                .verificationStatus(verification.getStatus())
                .rejectReason(verification.getRejectReason())
                .submitTime(verification.getSubmitTime())
                .auditTime(verification.getAuditTime())
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

    private TransitRecordVo convertToTransitRecordVo(TransitRecord transitRecord) {
        String entrySiteName = null;
        String exitSiteName = null;
        String city = null;
        String entryDeviceName = null;
        String exitDeviceName = null;
        
        // 查询进站站点信息
        if (transitRecord.getEntrySiteId() != null) {
            Site entrySite = siteMapper.selectById(transitRecord.getEntrySiteId());
            if (entrySite != null) {
                entrySiteName = entrySite.getSiteName();
                city = entrySite.getCity(); // 从进站站点获取城市信息
            }
        }
        
        // 查询出站站点信息
        if (transitRecord.getExitSiteId() != null) {
            Site exitSite = siteMapper.selectById(transitRecord.getExitSiteId());
            if (exitSite != null) {
                exitSiteName = exitSite.getSiteName();
                // 如果进站站点没有城市信息，从出站站点获取
                if (city == null) {
                    city = exitSite.getCity();
                }
            }
        }
        
        // 查询进站设备信息
        if (transitRecord.getEntryDeviceId() != null) {
            TurnstileDevice entryDevice = turnstileDeviceMapper.selectById(transitRecord.getEntryDeviceId());
            entryDeviceName = entryDevice != null ? entryDevice.getDeviceName() : null;
        }
        
        // 查询出站设备信息
        if (transitRecord.getExitDeviceId() != null) {
            TurnstileDevice exitDevice = turnstileDeviceMapper.selectById(transitRecord.getExitDeviceId());
            exitDeviceName = exitDevice != null ? exitDevice.getDeviceName() : null;
        }
        
        Long durationMinutes = null;
        if (transitRecord.getEntryTime() != null && transitRecord.getExitTime() != null) {
            long diffInSeconds = java.time.Duration.between(transitRecord.getEntryTime(), transitRecord.getExitTime()).getSeconds();
            durationMinutes = diffInSeconds / 60;
        }
        
        String statusName = getTransitStatusName(transitRecord.getStatus());
        
        return TransitRecordVo.builder()
                .userId(transitRecord.getUserId())
                .mode(transitRecord.getMode())
                .city(city)
                .entrySiteName(entrySiteName)
                .exitSiteName(exitSiteName)
                .entryDeviceName(entryDeviceName)
                .exitDeviceName(exitDeviceName)
                .entryTime(transitRecord.getEntryTime())
                .exitTime(transitRecord.getExitTime())
                .amount(transitRecord.getAmount())
                .discountAmount(transitRecord.getDiscountAmount())
                .actualAmount(transitRecord.getActualAmount())
                .status(transitRecord.getStatus())
                .statusName(statusName)
                .reason(transitRecord.getReason())
                .transactionId(transitRecord.getTransactionId())
                .durationMinutes(durationMinutes)
                .createdTime(transitRecord.getCreatedTime())
                .updatedTime(transitRecord.getUpdatedTime())
                .build();
    }
    
    private String getTransitStatusName(Integer status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case 1: return "已进站";
            case 2: return "已出站";
            case 3: return "行程完成";
            case 4: return "行程取消";
            default: return "未知状态";
        }
    }
}

package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.software.code.common.result.Result;
import org.software.code.common.util.JwtUtil;
import org.software.code.dto.UserAuditSubmitRequest;
import org.software.code.entity.UserAudit;
import org.software.code.mapper.UserAuditMapper;
import org.software.code.mapper.UserMapper;
import org.software.code.service.UserAuditService;
import org.software.code.vo.UserAuditStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户资料审核服务实现类
 */
@Service
public class UserAuditServiceImpl implements UserAuditService {

    @Autowired
    private UserAuditMapper userAuditMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 提交资料审核
     * 如果用户已经提交过，则更新之前的记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> submitAudit(String token, UserAuditSubmitRequest request) {
        try {
            // 从token中提取用户ID
            long userId = JwtUtil.extractID(token);
            
            // 查询用户是否已提交过审核
            UserAudit existingAudit = userAuditMapper.selectOne(
                new LambdaQueryWrapper<UserAudit>()
                    .eq(UserAudit::getUserId, userId)
                    .orderByDesc(UserAudit::getSubmitTime)
                    .last("LIMIT 1")
            );
            
            // 创建或更新审核记录
            UserAudit userAudit;
            if (existingAudit != null) {
                // 如果已经通过审核，不允许再次提交
                if ("approved".equals(existingAudit.getStatus())) {
                    return Result.failed("您的资料已通过审核，无需重新提交");
                }
                
                // 更新现有记录
                userAudit = existingAudit;
                userAudit.setRealName(request.getRealName());
                userAudit.setIdCardFront(request.getIdCardFrontUrl());
                userAudit.setIdCardBack(request.getIdCardBackUrl());
                userAudit.setStatus("pending"); // 重置为待审核状态
                userAudit.setRejectReason(""); // 清空拒绝原因
                userAudit.setSubmitTime(LocalDateTime.now());
                userAudit.setAuditTime(null); // 清空审核时间
                
                userAuditMapper.updateById(userAudit);
            } else {
                // 创建新记录
                userAudit = new UserAudit();
                userAudit.setUserId(userId);
                userAudit.setRealName(request.getRealName());
                userAudit.setIdCardFront(request.getIdCardFrontUrl());
                userAudit.setIdCardBack(request.getIdCardBackUrl());
                userAudit.setStatus("pending"); // 默认待审核
                userAudit.setRejectReason("");
                userAudit.setSubmitTime(LocalDateTime.now());
                
                userAuditMapper.insert(userAudit);
            }
            
            return Result.success("资料提交成功，等待审核");
        } catch (Exception e) {
            return Result.failed("提交失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询当前用户审核状态（详细版）
     */
    @Override
    public Result<UserAuditStatusVo> getAuditStatus(String token) {
        try {
            // 从token中提取用户ID
            long userId = JwtUtil.extractID(token);
            
            // 查询用户最新的审核记录
            UserAudit userAudit = userAuditMapper.selectOne(
                new LambdaQueryWrapper<UserAudit>()
                    .eq(UserAudit::getUserId, userId)
                    .orderByDesc(UserAudit::getSubmitTime)
                    .last("LIMIT 1")
            );
            
            if (userAudit == null) {
                // 未提交过审核
                return Result.success("未提交审核", UserAuditStatusVo.builder()
                    .status("none")
                    .build());
            }
            
            // 构建返回对象
            UserAuditStatusVo statusVo = UserAuditStatusVo.builder()
                .status(userAudit.getStatus())
                .realName(userAudit.getRealName())
                .rejectReason(userAudit.getRejectReason())
                .submitTime(userAudit.getSubmitTime())
                .auditTime(userAudit.getAuditTime())
                .build();
            
            return Result.success("查询成功", statusVo);
        } catch (Exception e) {
            return Result.failed("查询失败：" + e.getMessage(), UserAuditStatusVo.class);
        }
    }

} 
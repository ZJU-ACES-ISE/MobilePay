package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.software.code.common.result.Result;
import org.software.code.common.result.ResultEnum;
import org.software.code.common.util.JwtUtil;
import org.software.code.entity.UserBalance;
import org.software.code.mapper.UserBalanceMapper;
import org.software.code.service.UserPaymentService;
import org.software.code.vo.UserBalanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserBalanceMapper userBalanceMapper;

    @Override
    public Result<UserBalanceVo> getUserBalance(String authorization) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = jwtUtil.extractID(token);
            
            // 从数据库获取用户余额
            QueryWrapper<UserBalance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            UserBalance userBalance = userBalanceMapper.selectOne(queryWrapper);
            
            if (userBalance == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "用户余额信息不存在", null);
            }
            
            // 构建返回结果
            UserBalanceVo balanceVo = UserBalanceVo.builder()
                    .userId(userId.toString())
                    .balance(userBalance.getBalance().toString())
                    .build();
            
            return Result.success("查询成功", balanceVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
} 
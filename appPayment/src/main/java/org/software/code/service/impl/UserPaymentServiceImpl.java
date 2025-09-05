package org.software.code.service.impl;

import org.software.code.common.result.Result;
import org.software.code.common.result.ResultEnum;
import org.software.code.common.util.JwtUtil;
import org.software.code.client.AssetsClient;
import org.software.code.service.UserPaymentService;
import org.software.code.vo.UserBalanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * 用户服务实现类
 */
@Service
public class UserPaymentServiceImpl implements UserPaymentService {


    
    @Autowired
    private AssetsClient assetsClient;

    @Override
    public Result<UserBalanceVo> getUserBalance(String authorization) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 通过Feign调用获取用户余额
            BigDecimal balanceResult = assetsClient.getUserBalance(userId);
            
            if (balanceResult == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "用户余额信息不存在", null);
            }
            
            // 构建返回结果
            UserBalanceVo balanceVo = UserBalanceVo.builder()
                    .userId(userId.toString())
                    .balance(balanceResult.toString())
                    .build();
            
            return Result.success("查询成功", balanceVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
} 
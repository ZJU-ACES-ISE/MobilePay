package org.software.code.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.software.code.entity.UserBalance;
import org.software.code.mapper.UserBalanceMapper;
import org.software.code.service.UserBalanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@DS("assetsdb")
public class UserBalanceServiceImpl extends ServiceImpl<UserBalanceMapper, UserBalance> implements UserBalanceService {

    @Resource
    UserBalanceMapper userBalanceMapper;

    @Override
    public UserBalance getBalanceByUser(Long userId) {
        return userBalanceMapper.selectOne(
            Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserId, userId)
        );
    }
}

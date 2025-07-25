package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.util.RedisUtil;
import org.software.code.dto.BankTransferDto;
import org.software.code.entity.BankCard;
import org.software.code.entity.TransferRecord;
import org.software.code.entity.User;
import org.software.code.entity.UserBalance;
import org.software.code.mapper.AssetsMapper;
import org.software.code.mapper.BillsMapper;
import org.software.code.mapper.CardsMapper;
import org.software.code.mapper.UserMapper;
import org.software.code.service.AssetsService;
import org.software.code.vo.AssertsChartItemVo;
import org.software.code.vo.BalanceSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AssetsServiceImpl implements AssetsService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    AssetsMapper assetsMapper;
    @Autowired
    CardsMapper cardsMapper;
    @Autowired
    BillsMapper billsMapper;
    @Autowired
    UserMapper userMapper;

    @Transactional
    @Override
    public void topUp(BankTransferDto bankTransferDto) {
        // 查询银行卡信息
        BankCard bankCard = cardsMapper.selectById(bankTransferDto.getBankCardId());
        if (bankCard == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        //用户id获取
        Long userId = bankCard.getUserId();
        // 校验支付密码
        User user = userMapper.selectById(userId);
        if (!passwordEncoder.matches(bankTransferDto.getPayPassword(), user.getPayPassword())) {
            throw new BusinessException(ExceptionEnum.PAY_PASSWORD_INVALID);
        }

        // 模拟校验银行卡余额（此处假设银行卡余额无限）

        // 更新用户余额（插入或更新）
        QueryWrapper<UserBalance> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        UserBalance userBalance = assetsMapper.selectOne(wrapper);

        if (userBalance == null) {
            userBalance = new UserBalance();
            userBalance.setUserId(userId);
            userBalance.setBalance(bankTransferDto.getAmount());
            userBalance.setUpdateTime(LocalDateTime.now());
            assetsMapper.insert(userBalance);
        } else {
            userBalance.setBalance(userBalance.getBalance().add(bankTransferDto.getAmount()));
            userBalance.setUpdateTime(LocalDateTime.now());
            assetsMapper.updateById(userBalance);
        }

        // 插入交易记录
        TransferRecord record = new TransferRecord();
        record.setTransferNumber(UUID.randomUUID().toString());
        record.setUserId(userId);
        record.setUserName(user.getNickname()); // 实际应查询用户信息
        record.setType(1); // 1 = 转入
        record.setBankCardId(bankTransferDto.getBankCardId());
        record.setTargetId(bankTransferDto.getBankCardId());
        record.setTargetType(3); // 银行卡
        record.setTargetName(bankTransferDto.getBankName());
        record.setAmount(bankTransferDto.getAmount());
        record.setBizCategory(4); // 默认分类
        record.setCompleteTime(LocalDateTime.now());
        record.setRemark("用户充值");

        billsMapper.insert(record);
    }

    @Transactional
    @Override
    public void withdraw(BankTransferDto bankTransferDto) {
        BankCard bankCard = cardsMapper.selectById(bankTransferDto.getBankCardId());
        if (bankCard == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        Long userId = bankCard.getUserId();
        User user = userMapper.selectById(userId);
        if (!passwordEncoder.matches(bankTransferDto.getPayPassword(), user.getPayPassword())) {
            throw new BusinessException(ExceptionEnum.PAY_PASSWORD_INVALID);
        }

        QueryWrapper<UserBalance> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserBalance userBalance = assetsMapper.selectOne(wrapper);

        if (userBalance == null || userBalance.getBalance().compareTo(bankTransferDto.getAmount()) < 0) {
            throw new BusinessException(ExceptionEnum.BALANCE_NOT_ENOUGH);
        }

        userBalance.setBalance(userBalance.getBalance().subtract(bankTransferDto.getAmount()));
        userBalance.setUpdateTime(LocalDateTime.now());
        assetsMapper.updateById(userBalance);

        TransferRecord record = new TransferRecord();
        record.setTransferNumber(UUID.randomUUID().toString());
        record.setUserId(userId);
        record.setUserName(user.getNickname());
        record.setType(2); // 2 = 转出
        record.setBankCardId(bankTransferDto.getBankCardId());
        record.setTargetId(bankTransferDto.getBankCardId());
        record.setTargetType(3); // 银行卡
        record.setTargetName(bankTransferDto.getBankName());
        record.setAmount(bankTransferDto.getAmount());
        record.setBizCategory(4); // 默认分类
        record.setCompleteTime(LocalDateTime.now());
        record.setRemark("用户提现");

        billsMapper.insert(record);
    }

    //利用定时任务task
    @Override
    public BalanceSummaryVo getBalanceSummary(Long userId) {
        // 1. 查询当前用户余额
        UserBalance userBalance = assetsMapper.selectOne(
                new QueryWrapper<UserBalance>().eq("user_id", userId)
        );
        if (userBalance == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        // 2. 构造最近 7 天的资产折线图数据
        List<AssertsChartItemVo> chartList = new ArrayList<>();
        DateTimeFormatter redisKeyFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 6; i > 0; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            String keyDate = day.format(redisKeyFormatter);
            String displayDate = day.format(displayFormatter);

            String key = "balance:history:" + userId + ":" + keyDate;
            String cachedBalance = redisUtil.getValue(key);

            BigDecimal balance = (cachedBalance != null) ? new BigDecimal(cachedBalance) : BigDecimal.ZERO;

            AssertsChartItemVo item = new AssertsChartItemVo();
            item.setDay(displayDate);
            item.setTotal_expense(balance.toPlainString());

            chartList.add(item);
        }

        // 当天：用数据库当前余额
        LocalDate today = LocalDate.now();
        AssertsChartItemVo todayItem = new AssertsChartItemVo();
        todayItem.setDay(today.format(displayFormatter));
        todayItem.setTotal_expense(userBalance.getBalance().toPlainString());
        chartList.add(todayItem);

        // 3. 封装返回对象
        BalanceSummaryVo balanceV0 = new BalanceSummaryVo();
        balanceV0.setId(userBalance.getId());
        balanceV0.setBalance(userBalance.getBalance().toPlainString());
        balanceV0.setUpdateTime(userBalance.getUpdateTime().toString());
        balanceV0.setAssertsChartV0(chartList);

        return balanceV0;
    }
}

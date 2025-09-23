package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.util.RedisUtil;
import org.software.code.dto.BankTransferDto;
import org.software.code.entity.BankCard;
import org.software.code.entity.TransferRecord;
import org.software.code.entity.UserBalance;
import org.software.code.mapper.AssetsMapper;
import org.software.code.mapper.BillsMapper;
import org.software.code.mapper.CardsMapper;
import org.software.code.client.UserClient;
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
import java.util.*;
import org.software.code.common.result.Result;
import java.util.concurrent.TimeUnit;

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
    UserClient userClient;

    @Transactional
    @Override
    public void topUp(BankTransferDto bankTransferDto, Long uid) {
        // 查询银行卡信息
        BankCard bankCard = cardsMapper.selectById(bankTransferDto.getBankCardId());
        if (bankCard == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        //用户id获取
        Long userId = bankCard.getUserId();

        //银行卡合法检验
        if (!Objects.equals(bankCard.getUserId(), uid))
            throw new BusinessException(ExceptionEnum.BANK_CARD_NOT_CORRECT);

        // 校验支付密码 - 通过Feign调用appUser服务
        if (!userClient.verifyPayPassword(userId, bankTransferDto.getPayPassword())) {
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
        record.setUserName(userClient.getUserNickname(userId)); // 通过Feign调用获取用户昵称
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
    public void withdraw(BankTransferDto bankTransferDto, Long uid) {
        BankCard bankCard = cardsMapper.selectById(bankTransferDto.getBankCardId());
        if (bankCard == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        //用户id获取
        Long userId = bankCard.getUserId();

        //银行卡合法检验
        if (!Objects.equals(bankCard.getUserId(), uid))
            throw new BusinessException(ExceptionEnum.BANK_CARD_NOT_CORRECT);

        // 校验支付密码 - 通过Feign调用appUser服务
        if (!userClient.verifyPayPassword(userId, bankTransferDto.getPayPassword())) {
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
        record.setUserName(userClient.getUserNickname(userId));
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
        // 1. 获取当前余额
        UserBalance userBalance = assetsMapper.selectOne(
                new QueryWrapper<UserBalance>().eq("user_id", userId)
        );
        if (userBalance == null) {
            throw new BusinessException(ExceptionEnum.DATA_NOT_FOUND);
        }

        List<AssertsChartItemVo> chartList = new ArrayList<>();
        DateTimeFormatter redisKeyFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MM-dd");

        // 从今天开始回溯 6 天
        Map<LocalDate, BigDecimal> dailyBalanceMap = new LinkedHashMap<>();

        // 当前余额作为起点
        LocalDate today = LocalDate.now();
        BigDecimal currentBalance = userBalance.getBalance();

        for (int i = 0; i <= 6; i++) {
            LocalDate day = today.minusDays(i);
            String redisKey = "balance:history:" + userId + ":" + day.format(redisKeyFormatter);

            String cached = redisUtil.getValue(redisKey);
            BigDecimal dayBalance;

            if (i == 0) {
                // 1. 第一天（今天）没有缓存，直接用当前数据库余额
                dayBalance = currentBalance;
            }
            else if (cached != null) {
                // 2. 如果有缓存，直接使用
                dayBalance = new BigDecimal(cached);
            }
            else {
                // 3. 计算：下一天的余额 - 下一天的收入 + 下一天的支出
                LocalDate nextDay = day.plusDays(1);
                LocalDateTime start = nextDay.atStartOfDay();
                LocalDateTime end = nextDay.plusDays(1).atStartOfDay();

                // 查找 nextDay 当天所有支出和收入
                BigDecimal income = billsMapper.sumAmountByUserIdAndTypeAndTime(userId, 1, start, end); // type 1: 转入
                BigDecimal expense = billsMapper.sumAmountByUserIdAndTypeAndTime(userId, 2, start, end);// type 2: 转出

                BigDecimal nextBalance = dailyBalanceMap.get(nextDay);
                if (nextBalance == null){
                    nextBalance = BigDecimal.ZERO;
                }

                dayBalance = nextBalance.subtract(income).add(expense);

                // 存入 redis，过期时间 7 天
                redisUtil.setValue(redisKey, dayBalance.toPlainString());
                redisUtil.setExpire(redisKey, 7, TimeUnit.DAYS);
//                redisUtil.setExpire(redisKey, 7, TimeUnit.SECONDS);
            }

            // 加入 map
            dailyBalanceMap.put(day, dayBalance);
        }

        // 倒序构造图表数据（时间从早到晚）
        dailyBalanceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    AssertsChartItemVo item = new AssertsChartItemVo();
                    item.setDay(entry.getKey().format(displayFormatter));
                    item.setBalance(entry.getValue().toPlainString());
                    chartList.add(item);
                });

        // 组装 VO
        BalanceSummaryVo vo = new BalanceSummaryVo();
        vo.setId(String.valueOf(userBalance.getId()));
        vo.setBalance(userBalance.getBalance().toPlainString());
        vo.setUpdateTime(userBalance.getUpdateTime().toString());
        vo.setAssertsChartV0(chartList);

        return vo;
    }

    @Override
    public BigDecimal getUserBalance(Long userId) {
        QueryWrapper<UserBalance> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserBalance userBalance = assetsMapper.selectOne(wrapper);
        return userBalance != null ? userBalance.getBalance() : BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public void deductBalance(Long userId, BigDecimal amount, String description) {
        QueryWrapper<UserBalance> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserBalance userBalance = assetsMapper.selectOne(wrapper);
        
        if (userBalance == null || userBalance.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(ExceptionEnum.BALANCE_INSUFFICIENT);
        }
        
        userBalance.setBalance(userBalance.getBalance().subtract(amount));
        userBalance.setUpdateTime(LocalDateTime.now());
        assetsMapper.updateById(userBalance);
        
        // 创建交易记录
        createTransferRecord(userId, amount, 2, description); // 2 = 支出
    }

    @Override
    @Transactional
    public void addBalance(Long userId, BigDecimal amount, String description) {
        QueryWrapper<UserBalance> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserBalance userBalance = assetsMapper.selectOne(wrapper);
        
        if (userBalance == null) {
            userBalance = new UserBalance();
            userBalance.setUserId(userId);
            userBalance.setBalance(amount);
            userBalance.setUpdateTime(LocalDateTime.now());
            assetsMapper.insert(userBalance);
        } else {
            userBalance.setBalance(userBalance.getBalance().add(amount));
            userBalance.setUpdateTime(LocalDateTime.now());
            assetsMapper.updateById(userBalance);
        }
        
        // 创建交易记录
        createTransferRecord(userId, amount, 1, description); // 1 = 收入
    }

    @Override
    public BalanceSummaryVo getUserAssetsStatistics(Long userId) {
        // 获取用户余额统计
        return getBalanceSummary(userId);
    }

    @Override
    public List<BankCard> getUserCards(Long userId) {
        QueryWrapper<BankCard> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return cardsMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void createTransactionRecord(Object transactionRecord) {
        if (transactionRecord instanceof Map) {
            Map<String, Object> recordMap = (Map<String, Object>) transactionRecord;
            TransferRecord record = new TransferRecord();
            
            record.setTransferNumber((String) recordMap.get("transferNumber"));
            record.setUserId(((Number) recordMap.get("userId")).longValue());
            record.setUserName((String) recordMap.get("userName"));
            record.setType(((Number) recordMap.get("type")).intValue());
            record.setTargetId(((Number) recordMap.get("targetId")).longValue());
            record.setTargetType(((Number) recordMap.get("targetType")).intValue());
            record.setTargetName((String) recordMap.get("targetName"));
            record.setBizCategory(((Number) recordMap.get("bizCategory")).intValue());
            record.setAmount((BigDecimal) recordMap.get("amount"));
            record.setCompleteTime((LocalDateTime) recordMap.get("completeTime"));
            record.setRemark((String) recordMap.get("remark"));
            
            billsMapper.insert(record);
        }
    }

    

    @Override
    @Transactional
    public Result<Boolean> createUserBalance(Long userId, BigDecimal balance) {
        try {
            UserBalance userBalance = new UserBalance();
            userBalance.setUserId(userId);
            userBalance.setBalance(balance);
            userBalance.setUpdateTime(LocalDateTime.now());
            
            assetsMapper.insert(userBalance);
            return Result.success(true);
        } catch (Exception e) {
            return Result.success(false);
        }
    }

    /**
     * 创建转账记录
     */
    private void createTransferRecord(Long userId, BigDecimal amount, Integer type, String description) {
        // 通过UserClient获取用户昵称
        String nickname = userClient.getUserNickname(userId);
        
        TransferRecord record = new TransferRecord();
        record.setTransferNumber(UUID.randomUUID().toString());
        record.setUserId(userId);
        record.setUserName(nickname);
        record.setType(type);
        record.setTargetId(userId);
        record.setTargetType(1); // 用户
        record.setTargetName(nickname);
        record.setAmount(amount);
        record.setBizCategory(4); // 其他
        record.setCompleteTime(LocalDateTime.now());
        record.setRemark(description);
        
        billsMapper.insert(record);
    }
}

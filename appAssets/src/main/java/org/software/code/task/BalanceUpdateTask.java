package org.software.code.task;

import lombok.extern.slf4j.Slf4j;
import org.software.code.common.util.RedisUtil;
import org.software.code.entity.UserBalance;
import org.software.code.mapper.AssetsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class BalanceUpdateTask {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AssetsMapper assetsMapper;

    @PostConstruct
    public void init() {
        log.info("系统启动中，尝试补充昨日余额快照...");
        snapshotUserBalanceToRedis();
    }

    /**
     * 每天 00:00:01 执行，将每个用户当前余额保存为昨日余额，缓存 7 天
     */
    @Scheduled(cron = "1 0 0 * * ?")
    public void snapshotUserBalanceToRedis() {
        log.info("【定时任务】开始快照用户余额");

        // 获取所有用户余额
        List<UserBalance> allBalances = assetsMapper.selectList(null);

        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd

        for (UserBalance ub : allBalances) {
            Long userId = ub.getUserId();
            BigDecimal balance = ub.getBalance();
            
            String redisKey = String.format("balance:history:%d:%s", userId, today);
            Boolean exists = redisUtil.hasKey(redisKey);
            if (Boolean.TRUE.equals(exists)) {
                continue; // 已存在则跳过
            }
            redisUtil.setValue (redisKey, balance.toPlainString());
            redisUtil.setExpire(redisKey, 7, TimeUnit.DAYS);
        }

        log.info("【定时任务】用户余额快照完成，共处理 {} 条记录", allBalances.size());
    }
}
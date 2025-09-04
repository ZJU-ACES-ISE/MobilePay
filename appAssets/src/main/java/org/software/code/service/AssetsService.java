package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.BankTransferDto;
import org.software.code.entity.BankCard;
import org.software.code.entity.TransferRecord;
import org.software.code.vo.BalanceSummaryVo;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AssetsService {
    /**
     * 余额充值，使用银行卡
     *
     * @param bankTransferDto 充值请求数据
     */
    void topUp(BankTransferDto bankTransferDto);

    /**
     * 余额提现，使用银行卡
     *
     * @param bankTransferDto 充值请求数据
     */
    void withdraw(BankTransferDto bankTransferDto);

    /**
     * 总资产展示
     *
     * @param uid 用户id
     */
    BalanceSummaryVo getBalanceSummary(Long uid);

    /**
     * 获取用户余额
     *
     * @param userId 用户ID
     * @return 用户余额
     */
    BigDecimal getUserBalance(Long userId);

    /**
     * 扣减用户余额
     *
     * @param userId 用户ID
     * @param amount 扣减金额
     * @param description 描述
     */
    void deductBalance(Long userId, BigDecimal amount, String description);

    /**
     * 增加用户余额
     *
     * @param userId 用户ID
     * @param amount 增加金额
     * @param description 描述
     */
    void addBalance(Long userId, BigDecimal amount, String description);

    /**
     * 获取用户资产统计
     *
     * @param userId 用户ID
     * @return 资产统计信息
     */
    BalanceSummaryVo getUserAssetsStatistics(Long userId);

    /**
     * 获取用户银行卡信息
     *
     * @param userId 用户ID
     * @return 银行卡信息
     */
    List<BankCard> getUserCards(Long userId);

    /**
     * 创建交易记录
     *
     * @param transactionRecord 交易记录
     */
    void createTransactionRecord(Object transactionRecord);
    
    /**
     * 创建用户余额记录
     */
    Result<Boolean> createUserBalance(Long userId, BigDecimal balance);
}

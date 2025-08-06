package org.software.code.service;

import org.software.code.dto.BankTransferDto;
import org.software.code.vo.BalanceSummaryVo;

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
}

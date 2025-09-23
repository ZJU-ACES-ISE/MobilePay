package org.software.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.software.code.entity.UserBalance;

public interface UserBalanceService extends IService<UserBalance> {

    UserBalance getBalanceByUser(Long userId);
}

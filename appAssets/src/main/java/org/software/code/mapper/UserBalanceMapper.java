package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.UserBalance;

/**
 * 用户余额Mapper接口
 */
@Mapper
public interface UserBalanceMapper extends BaseMapper<UserBalance> {
} 
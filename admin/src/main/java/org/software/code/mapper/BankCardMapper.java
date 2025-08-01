package org.software.code.mapper;

import org.software.code.entity.BankCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户银行卡表（卡包） Mapper 接口
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Mapper
public interface BankCardMapper extends BaseMapper<BankCard> {

}

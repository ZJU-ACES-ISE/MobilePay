package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.ReceiptTransaction;

/**
 * 收款交易记录数据访问层
 */
@Mapper
public interface ReceiptTransactionMapper extends BaseMapper<ReceiptTransaction> {
}

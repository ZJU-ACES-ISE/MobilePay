package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.TransactionRecord;

/**
 * 交易流水记录数据访问层
 */
@Mapper
public interface TransactionRecordMapper extends BaseMapper<TransactionRecord> {
}

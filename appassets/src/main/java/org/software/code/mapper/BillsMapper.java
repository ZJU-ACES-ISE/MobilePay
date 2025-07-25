package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.TransferRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper
public interface BillsMapper extends BaseMapper<TransferRecord> {
    BigDecimal sumAmountByUserIdAndTypeAndTime(Long userId, int i, LocalDateTime start, LocalDateTime end);
}

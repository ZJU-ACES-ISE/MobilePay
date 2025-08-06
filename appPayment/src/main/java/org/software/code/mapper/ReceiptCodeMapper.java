package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.ReceiptCode;

/**
 * 收款码数据访问层
 */
@Mapper
public interface ReceiptCodeMapper extends BaseMapper<ReceiptCode> {
}

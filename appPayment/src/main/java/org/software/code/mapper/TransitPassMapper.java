package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.software.code.entity.TransitPass;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通行证数据访问层
 */
@Mapper
public interface TransitPassMapper extends BaseMapper<TransitPass> {
    // 所有基本CRUD操作由BaseMapper提供
} 
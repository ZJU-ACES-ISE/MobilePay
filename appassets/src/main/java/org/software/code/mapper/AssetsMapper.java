package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.UserBalance;

@Mapper
public interface AssetsMapper extends BaseMapper<UserBalance> {
}

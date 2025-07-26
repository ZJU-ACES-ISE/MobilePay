package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.UserAudit;

/**
 * 用户资料审核Mapper接口
 */
@Mapper
public interface UserAuditMapper extends BaseMapper<UserAudit> {
} 
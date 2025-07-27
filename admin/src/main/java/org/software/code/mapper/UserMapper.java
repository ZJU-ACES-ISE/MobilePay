package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.software.code.entity.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

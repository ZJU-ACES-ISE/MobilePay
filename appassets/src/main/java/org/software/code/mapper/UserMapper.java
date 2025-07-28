package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.software.code.entity.User;

import java.util.List;

/**
 * 用户表 Mapper 接口，继承 MyBatis-Plus 的 BaseMapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select id from mobilepay.user")
    List<Long> selectAllUserIds();
    // BaseMapper 已提供基础的 CRUD 操作，不需要额外定义方法
    // 如需自定义复杂查询，可以在此添加
} 
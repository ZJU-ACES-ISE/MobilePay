package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.entity.TransitRecord;

import java.util.List;
import java.util.Map;

/**
 * 出行记录Mapper接口
 */
@Mapper
public interface TransitRecordMapper extends BaseMapper<TransitRecord> {
    
    /**
     * 查询用户的出行记录，包含站点信息
     * @param userId 用户ID
     * @param limit 限制记录数
     * @return 出行记录列表，包含站点信息
     */
    List<Map<String, Object>> selectUserTransitRecordsWithSites(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    // 不需要selectUnfinishedTransitRecords方法，可以使用MyBatis-Plus的QueryWrapper实现
} 
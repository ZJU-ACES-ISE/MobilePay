package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.entity.SiteFare;

import java.util.List;
import java.util.Map;

/**
 * 站点间费用Mapper接口
 */
@Mapper
public interface SiteFareMapper extends BaseMapper<SiteFare> {
    
    /**
     * 查询站点间费用，带站点名称
     * @param cityCode 城市编码
     * @param transitType 交通类型
     * @param fromSiteId 起始站点ID
     * @param toSiteId 终点站点ID
     * @return 站点间费用列表，包含站点名称
     */
    List<Map<String, Object>> selectSiteFareWithSiteNames(
            @Param("cityCode") String cityCode,
            @Param("transitType") String transitType,
            @Param("fromSiteId") Long fromSiteId,
            @Param("toSiteId") Long toSiteId);
    
    // 不需要selectFareByFromAndToSite方法，可以使用MyBatis-Plus的QueryWrapper实现
} 
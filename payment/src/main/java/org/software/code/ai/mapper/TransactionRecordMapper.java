package org.software.code.ai.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.ai.model.TransactionRecord;

import java.util.Date;
import java.util.List;

/**
 * 交易记录 Mapper
 */
@Mapper
public interface TransactionRecordMapper {
    
    /**
     * 获取用户指定时间范围内的交易记录
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 交易记录列表
     */
    List<TransactionRecord> listByUserIdAndTimeRange(
            @Param("userId") Long userId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
    
    /**
     * 获取用户最近N笔交易记录
     * @param userId 用户ID
     * @param limit 限制条数
     * @return 交易记录列表
     */
    List<TransactionRecord> listRecentByUserId(
            @Param("userId") Long userId,
            @Param("limit") int limit);
    
    /**
     * 获取用户指定时间范围内各分类的消费统计
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分类统计列表
     */
    List<CategoryStatistic> getCategoryStatistics(
            @Param("userId") Long userId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
    
    /**
     * 分类统计结果
     */
    interface CategoryStatistic {
        String getCategory();
        Double getTotalAmount();
        Integer getTransactionCount();
    }
}

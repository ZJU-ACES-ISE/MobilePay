package org.software.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.entity.TransactionRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易记录Mapper
 */
@Mapper
public interface TransactionRecordMapper extends BaseMapper<TransactionRecord> {
    
    /**
     * 根据用户ID和时间范围查询交易记录
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 交易记录列表
     */
    List<TransactionRecord> selectByUserIdAndTimeRange(@Param("userId") Long userId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据用户ID查询月度交易记录
     * @param userId 用户ID
     * @param year 年份
     * @param month 月份
     * @return 交易记录列表
     */
    List<TransactionRecord> selectByUserIdAndMonth(@Param("userId") Long userId,
                                                  @Param("year") Integer year,
                                                  @Param("month") Integer month);
    
    /**
     * 统计用户月度消费总额
     * @param userId 用户ID
     * @param year 年份
     * @param month 月份
     * @return 消费总额
     */
    Double sumAmountByUserIdAndMonth(@Param("userId") Long userId,
                                    @Param("year") Integer year,
                                    @Param("month") Integer month);
    
    /**
     * 按类别统计用户消费
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 按类别分组的消费统计
     */
    List<Object> groupByCategory(@Param("userId") Long userId,
                                @Param("startTime") LocalDateTime startTime,
                                @Param("endTime") LocalDateTime endTime);
} 
package org.software.code.service;

import org.software.code.entity.DiscountStrategy;
import org.software.code.vo.OrderContext;
import org.software.code.dto.DiscountStrategyCreateDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 折扣服务接口
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface DiscountService {
    
    /**
     * 计算折扣金额
     * 
     * @param originalAmount 原始金额
     * @param context 订单上下文
     * @return 折扣金额
     */
    BigDecimal calculateDiscount(BigDecimal originalAmount, OrderContext context);
    
    /**
     * 获取可用的折扣策略列表
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param cityCode 城市编码
     * @return 可用的折扣策略列表
     */
    List<DiscountStrategy> getAvailableStrategies(Long userId, Long targetId, String cityCode);
    
    /**
     * 根据策略类型获取最佳折扣策略
     * 
     * @param strategyType 策略类型
     * @param userId 用户ID
     * @param cityCode 城市编码
     * @return 最佳折扣策略
     */
    DiscountStrategy getBestStrategy(String strategyType, Long userId, String cityCode);
    
    /**
     * 根据策略ID获取折扣策略
     * 
     * @param strategyId 策略ID
     * @return 折扣策略
     */
    DiscountStrategy getStrategyById(Long strategyId);
    
    /**
     * 创建折扣策略
     * 
     * @param createDto 创建折扣策略DTO
     * @param createdBy 创建者ID
     * @return 创建的折扣策略
     */
    DiscountStrategy createDiscountStrategy(DiscountStrategyCreateDto createDto, Long createdBy);
} 
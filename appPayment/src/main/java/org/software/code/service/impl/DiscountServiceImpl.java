package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.entity.DiscountStrategy;
import org.software.code.mapper.DiscountStrategyMapper;
import org.software.code.service.DiscountService;
import org.software.code.strategy.DiscountCalculationStrategy;
import org.software.code.strategy.DiscountStrategyFactory;
import org.software.code.vo.OrderContext;
import org.software.code.common.constants.DiscountConstants;
import org.software.code.dto.DiscountStrategyCreateDto;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 折扣服务实现类
 * 
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Service
public class DiscountServiceImpl implements DiscountService {
    
    private static final Logger logger = LoggerFactory.getLogger(DiscountServiceImpl.class);
    
    @Autowired
    private DiscountStrategyMapper discountStrategyMapper;
    
    @Autowired
    private DiscountStrategyFactory strategyFactory;
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal originalAmount, OrderContext context) {
        if (context.getDiscountStrategy() == null) {
            logger.warn("未找到可用的折扣策略");
            return BigDecimal.ZERO;
        }
        
        DiscountStrategy strategy = context.getDiscountStrategy();
        
        // 检查策略是否有效
        if (!isStrategyValid(strategy)) {
            logger.warn("折扣策略无效或已过期，策略ID：{}", strategy.getId());
            return BigDecimal.ZERO;
        }
        
        // 获取对应的策略实现
        DiscountCalculationStrategy calculationStrategy = strategyFactory.getStrategy(strategy.getDiscountType());
        if (calculationStrategy == null) {
            logger.warn("未找到对应的折扣计算策略，策略类型：{}", strategy.getDiscountType());
            return BigDecimal.ZERO;
        }
        
        // 计算折扣
        BigDecimal discount = calculationStrategy.calculate(originalAmount, context);
        
        logger.info("计算折扣完成，原始金额：{}，折扣金额：{}，策略ID：{}", 
                   originalAmount, discount, strategy.getId());
        
        return discount;
    }
    
    @Override
    public List<DiscountStrategy> getAvailableStrategies(Long userId, Long targetId, String cityCode) {
        LambdaQueryWrapper<DiscountStrategy> wrapper = Wrappers.<DiscountStrategy>lambdaQuery()
                .eq(DiscountStrategy::getStatus, DiscountConstants.Status.ACTIVE)
                .and(w -> w.isNull(DiscountStrategy::getStartTime)
                         .or().le(DiscountStrategy::getStartTime, LocalDateTime.now()))
                .and(w -> w.isNull(DiscountStrategy::getEndTime)
                         .or().ge(DiscountStrategy::getEndTime, LocalDateTime.now()));
        
        // 如果指定了城市，筛选适用城市的策略
        if (StringUtils.hasText(cityCode)) {
            wrapper.and(w -> w.isNull(DiscountStrategy::getTargetCities)
                            .or().like(DiscountStrategy::getTargetCities, cityCode));
        }
        
        // 检查使用次数限制
        wrapper.and(w -> w.isNull(DiscountStrategy::getUsageLimit)
                        .or().apply("used_count < usage_limit"));
        
        return discountStrategyMapper.selectList(wrapper);
    }
    
    @Override
    public DiscountStrategy getBestStrategy(String strategyType, Long userId, String cityCode) {
        List<DiscountStrategy> strategies = getAvailableStrategies(userId, null, cityCode);
        
        // 筛选指定类型的策略
        return strategies.stream()
                .filter(s -> strategyType.equals(s.getStrategyType()))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public DiscountStrategy getStrategyById(Long strategyId) {
        if (strategyId == null) {
            return null;
        }
        
        DiscountStrategy strategy = discountStrategyMapper.selectById(strategyId);
        
        // 检查策略是否有效
        if (strategy != null && !isStrategyValid(strategy)) {
            logger.warn("折扣策略无效或已过期，策略ID：{}", strategyId);
            return null;
        }
        
        return strategy;
    }
    
    @Override
    @Transactional
    public DiscountStrategy createDiscountStrategy(DiscountStrategyCreateDto createDto, Long createdBy) {
        try {
            // 检查策略名称是否已存在
            long count = discountStrategyMapper.selectCount(
                Wrappers.<DiscountStrategy>lambdaQuery()
                    .eq(DiscountStrategy::getStrategyName, createDto.getStrategyName())
            );
            
            if (count > 0) {
                logger.warn("策略名称已存在：{}", createDto.getStrategyName());
                return null;
            }
            
            // 创建新的折扣策略
            DiscountStrategy strategy = new DiscountStrategy();
            BeanUtils.copyProperties(createDto, strategy);
            
            strategy.setCreatedBy(createdBy);
            strategy.setStatus(DiscountConstants.Status.ACTIVE);
            strategy.setUsedCount(0);
            strategy.setCreatedTime(LocalDateTime.now());
            strategy.setUpdatedTime(LocalDateTime.now());
            
            // 保存到数据库
            discountStrategyMapper.insert(strategy);
            
            logger.info("折扣策略创建成功，策略ID：{}，策略名称：{}", strategy.getId(), strategy.getStrategyName());
            
            return strategy;
            
        } catch (Exception e) {
            logger.error("创建折扣策略失败，策略名称：{}，错误：{}", createDto.getStrategyName(), e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 检查策略是否有效
     */
    private boolean isStrategyValid(DiscountStrategy strategy) {
        if (!DiscountConstants.Status.ACTIVE.equals(strategy.getStatus())) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 检查开始时间
        if (strategy.getStartTime() != null && now.isBefore(strategy.getStartTime())) {
            return false;
        }
        
        // 检查结束时间
        if (strategy.getEndTime() != null && now.isAfter(strategy.getEndTime())) {
            return false;
        }
        
        // 检查使用次数限制
        if (strategy.getUsageLimit() != null && strategy.getUsedCount() != null) {
            if (strategy.getUsedCount() >= strategy.getUsageLimit()) {
                return false;
            }
        }
        
        return true;
    }
} 
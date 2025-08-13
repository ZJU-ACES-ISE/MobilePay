package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.consts.AdminConstants;
import org.software.code.dto.DiscountStrategyCreateDto;
import org.software.code.dto.DiscountStrategySearchDto;
import org.software.code.entity.DiscountStrategy;
import org.software.code.mapper.DiscountStrategyMapper;
import org.software.code.service.DiscountStrategyService;
import org.software.code.vo.DiscountStrategyDetailVo;
import org.software.code.vo.DiscountStrategyListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Service
public class DiscountStrategyServiceImpl extends ServiceImpl<DiscountStrategyMapper, DiscountStrategy> implements DiscountStrategyService {
    private static final Logger logger = LoggerFactory.getLogger(DiscountStrategyServiceImpl.class);

    @Resource
    private DiscountStrategyMapper discountStrategyMapper;

    @Override
    public Page<DiscountStrategyListVo> getDiscountStrategyPage(Integer pageNum, Integer pageSize, DiscountStrategySearchDto searchDto) {
        logger.info("获取折扣策略列表分页数据，页码：{}，页大小：{}", pageNum, pageSize);

        LambdaQueryWrapper<DiscountStrategy> wrapper = Wrappers.<DiscountStrategy>lambdaQuery();
        
        // 添加空安全检查和所有搜索条件
        if (searchDto != null) {
            // 关键字搜索（策略名称）
            if (StringUtils.hasText(searchDto.getKeyword())) {
                wrapper.like(DiscountStrategy::getStrategyName, searchDto.getKeyword());
            }
            
            // 策略状态过滤
            if (StringUtils.hasText(searchDto.getStatus())) {
                wrapper.eq(DiscountStrategy::getStatus, searchDto.getStatus());
            }
            
            // 策略类型过滤
            if (StringUtils.hasText(searchDto.getStrategyType())) {
                wrapper.eq(DiscountStrategy::getStrategyType, searchDto.getStrategyType());
            }
            
            // 折扣类型过滤
            if (StringUtils.hasText(searchDto.getDiscountType())) {
                wrapper.eq(DiscountStrategy::getDiscountType, searchDto.getDiscountType());
            }
            
            // 目标城市过滤
            if (StringUtils.hasText(searchDto.getTargetCity())) {
                wrapper.like(DiscountStrategy::getTargetCities, searchDto.getTargetCity());
            }
            
            // 创建者过滤
            if (searchDto.getCreatedBy() != null) {
                wrapper.eq(DiscountStrategy::getCreatedBy, searchDto.getCreatedBy());
            }
            
            // 创建时间范围过滤
            if (searchDto.getStartTime() != null) {
                wrapper.ge(DiscountStrategy::getCreatedTime, Timestamp.valueOf(searchDto.getStartTime()));
            }
            if (searchDto.getEndTime() != null) {
                wrapper.le(DiscountStrategy::getCreatedTime, Timestamp.valueOf(searchDto.getEndTime()));
            }
            
            // 有效期时间范围过滤
            if (searchDto.getValidStartTime() != null) {
                wrapper.ge(DiscountStrategy::getStartTime, Timestamp.valueOf(searchDto.getValidStartTime()));
            }
            if (searchDto.getValidEndTime() != null) {
                wrapper.le(DiscountStrategy::getEndTime, Timestamp.valueOf(searchDto.getValidEndTime()));
            }
            
            // 折扣金额范围过滤
            if (searchDto.getMinDiscountAmount() != null) {
                wrapper.ge(DiscountStrategy::getDiscountAmount, searchDto.getMinDiscountAmount());
            }
            if (searchDto.getMaxDiscountAmount() != null) {
                wrapper.le(DiscountStrategy::getDiscountAmount, searchDto.getMaxDiscountAmount());
            }
            
            // 是否包含过期策略（默认不包含）
            if (searchDto.getIncludeExpired() == null || !searchDto.getIncludeExpired()) {
                wrapper.ne(DiscountStrategy::getStatus, AdminConstants.DiscountStrategyStatus.EXPIRED);
            }
        }
        
        // 默认排序：按创建时间倒序  
        wrapper.orderByDesc(DiscountStrategy::getCreatedTime);

        Page<DiscountStrategy> resultPage = discountStrategyMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        Page<DiscountStrategyListVo> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");

        List<DiscountStrategyListVo> strategyListVos = resultPage.getRecords().stream()
                .map(this::convertToDiscountStrategyListVo)
                .collect(Collectors.toList());

        voPage.setRecords(strategyListVos);
        return voPage;
    }

    @Override
    public DiscountStrategyDetailVo getDiscountStrategyDetail(Long strategyId) {
        logger.info("获取折扣策略详细信息，策略ID：{}", strategyId);

        DiscountStrategy strategy = discountStrategyMapper.selectById(strategyId);
        if (strategy == null) {
            logger.warn("折扣策略不存在，策略ID：{}", strategyId);
            return null;
        }

        return convertToDiscountStrategyDetailVo(strategy);
    }

    @Override
    @Transactional
    public DiscountStrategyDetailVo createDiscountStrategy(DiscountStrategyCreateDto createDto, Long adminId) {
        logger.info("创建折扣策略，策略名称：{}，创建者：{}", createDto.getStrategyName(), adminId);

        try {
            // 检查策略名称是否已存在
            if (checkStrategyNameExists(createDto.getStrategyName(), null)) {
                logger.warn("策略名称已存在：{}", createDto.getStrategyName());
                return null;
            }

            DiscountStrategy strategy = new DiscountStrategy();
            BeanUtils.copyProperties(createDto, strategy);
            strategy.setCreatedBy(adminId);
            strategy.setStatus(AdminConstants.DiscountStrategyStatus.ACTIVE);
            strategy.setUsedCount(0);
            strategy.setCreatedTime(LocalDateTime.now());
            strategy.setUpdatedTime(LocalDateTime.now());
            discountStrategyMapper.insert(strategy);

            logger.info("折扣策略创建成功，策略ID：{}", strategy.getId());
            
            // 返回创建后的策略详细信息
            return getDiscountStrategyDetail(strategy.getId());

        } catch (Exception e) {
            logger.error("创建折扣策略失败，错误：{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public DiscountStrategyDetailVo updateDiscountStrategy(Long strategyId, DiscountStrategyCreateDto createDto, Long adminId) {
        logger.info("更新折扣策略，策略ID：{}，更新者：{}", strategyId, adminId);

        try {
            DiscountStrategy existingStrategy = discountStrategyMapper.selectById(strategyId);
            if (existingStrategy == null) {
                logger.warn("折扣策略不存在，策略ID：{}", strategyId);
                return null;
            }

            // 检查策略名称是否已存在（排除当前策略）
            if (checkStrategyNameExists(createDto.getStrategyName(), strategyId)) {
                logger.warn("策略名称已存在：{}", createDto.getStrategyName());
                return null;
            }

            BeanUtils.copyProperties(createDto, existingStrategy);
//            existingStrategy.setUpdatedTime(LocalDateTime.now());

            discountStrategyMapper.updateById(existingStrategy);

            logger.info("折扣策略更新成功，策略ID：{}", strategyId);
            
            // 返回更新后的策略详细信息
            return getDiscountStrategyDetail(strategyId);

        } catch (Exception e) {
            logger.error("更新折扣策略失败，策略ID：{}，错误：{}", strategyId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean deleteDiscountStrategy(Long strategyId, Long adminId) {
        logger.info("删除折扣策略，策略ID：{}，操作者：{}", strategyId, adminId);

        try {
            DiscountStrategy strategy = discountStrategyMapper.selectById(strategyId);
            if (strategy == null) {
                logger.warn("折扣策略不存在，策略ID：{}", strategyId);
                return false;
            }

            discountStrategyMapper.deleteById(strategyId);

            logger.info("折扣策略删除成功，策略ID：{}", strategyId);
            return true;

        } catch (Exception e) {
            logger.error("删除折扣策略失败，策略ID：{}，错误：{}", strategyId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean enableDiscountStrategy(Long strategyId, Long adminId) {
        logger.info("启用折扣策略，策略ID：{}，操作者：{}", strategyId, adminId);
        return updateDiscountStrategyStatus(strategyId, AdminConstants.DiscountStrategyStatus.ACTIVE, adminId);
    }

    @Override
    @Transactional
    public Boolean disableDiscountStrategy(Long strategyId, Long adminId) {
        logger.info("禁用折扣策略，策略ID：{}，操作者：{}", strategyId, adminId);
        return updateDiscountStrategyStatus(strategyId, AdminConstants.DiscountStrategyStatus.INACTIVE, adminId);
    }

    @Override
    public List<DiscountStrategyListVo> getActiveStrategiesByType(String strategyType) {
        logger.info("根据策略类型获取活跃策略列表，类型：{}", strategyType);

        List<DiscountStrategy> strategies = discountStrategyMapper.selectList(Wrappers.<DiscountStrategy>lambdaQuery()
                .eq(DiscountStrategy::getStrategyType, strategyType)
                .eq(DiscountStrategy::getStatus, AdminConstants.DiscountStrategyStatus.ACTIVE)
        );
        return strategies.stream()
                .map(this::convertToDiscountStrategyListVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscountStrategyListVo> getAvailableStrategiesByCity(String city) {
        logger.info("根据城市获取可用策略列表，城市：{}", city);

        List<DiscountStrategy> strategies = discountStrategyMapper.selectList(Wrappers.<DiscountStrategy>lambdaQuery()
                .like(DiscountStrategy::getTargetCities, city)
                .eq(DiscountStrategy::getStatus, AdminConstants.DiscountStrategyStatus.ACTIVE)
        );
        return strategies.stream()
                .map(this::convertToDiscountStrategyListVo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean batchUpdateStrategyStatus(List<Long> strategyIds, String status, Long adminId) {
        logger.info("批量更新策略状态，策略数量：{}，新状态：{}", strategyIds.size(), status);

        try {
            int updateCount = discountStrategyMapper.update(null, Wrappers.<DiscountStrategy>lambdaUpdate()
                    .set(DiscountStrategy::getStatus, status)
                    .in(DiscountStrategy::getId, strategyIds));
            logger.info("批量更新策略状态成功，影响行数：{}", updateCount);
            return updateCount > 0;

        } catch (Exception e) {
            logger.error("批量更新策略状态失败，错误：{}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public DiscountStrategyStatisticsVo getDiscountStrategyStatistics() {
        logger.info("获取折扣策略统计信息");

        DiscountStrategyStatisticsVo statistics = new DiscountStrategyStatisticsVo();

        // 统计各状态策略数量
        statistics.setActiveStrategies(countByStatus(AdminConstants.DiscountStrategyStatus.ACTIVE));
        statistics.setInactiveStrategies(countByStatus(AdminConstants.DiscountStrategyStatus.INACTIVE));
        statistics.setExpiredStrategies(countByStatus(AdminConstants.DiscountStrategyStatus.EXPIRED));

        // 统计各类型策略数量
        statistics.setTravelStrategies(countByStrategyType(AdminConstants.DiscountStrategyType.TRAVEL));
        statistics.setPaymentStrategies(countByStrategyType(AdminConstants.DiscountStrategyType.PAYMENT));
        statistics.setNewUserStrategies(countByStrategyType(AdminConstants.DiscountStrategyType.NEW_USER));
        statistics.setHolidayStrategies(countByStrategyType(AdminConstants.DiscountStrategyType.HOLIDAY));

        // 计算总策略数
        statistics.setTotalStrategies(
                statistics.getActiveStrategies() +
                statistics.getInactiveStrategies() +
                statistics.getExpiredStrategies()
        );

        return statistics;
    }


    private Boolean updateDiscountStrategyStatus(Long strategyId, String status, Long adminId) {
        try {
            DiscountStrategy strategy = discountStrategyMapper.selectById(strategyId);
            if (strategy == null) {
                return false;
            }

            strategy.setStatus(status);
//            strategy.setUpdatedTime(LocalDateTime.now());
            discountStrategyMapper.updateById(strategy);

            return true;

        } catch (Exception e) {
            logger.error("更新折扣策略状态失败，策略ID：{}，错误：{}", strategyId, e.getMessage(), e);
            return false;
        }
    }

    private DiscountStrategyListVo convertToDiscountStrategyListVo(DiscountStrategy strategy) {
        return DiscountStrategyListVo.builder()
                .strategyId(strategy.getId())
                .strategyName(strategy.getStrategyName())
                .strategyType(strategy.getStrategyType())
                .discountType(strategy.getDiscountType())
                .discountRate(strategy.getDiscountRate())
                .discountAmount(strategy.getDiscountAmount())
                .minAmount(strategy.getMinAmount())
                .maxDiscount(strategy.getMaxDiscount())
                .targetCitiesDisplay(parseTargetCitiesDisplay(strategy.getTargetCities()))
                .usageLimit(strategy.getUsageLimit())
                .usedCount(strategy.getUsedCount())
                .status(strategy.getStatus())
                .build();
    }

    private DiscountStrategyDetailVo convertToDiscountStrategyDetailVo(DiscountStrategy strategy) {
        DiscountStrategyDetailVo detailVo = DiscountStrategyDetailVo.builder()
                .strategyId(strategy.getId())
                .strategyName(strategy.getStrategyName())
                .strategyType(strategy.getStrategyType())
                .discountType(strategy.getDiscountType())
                .discountRate(strategy.getDiscountRate())
                .discountAmount(strategy.getDiscountAmount())
                .minAmount(strategy.getMinAmount())
                .maxDiscount(strategy.getMaxDiscount())
                .targetCities(strategy.getTargetCities())
                .usageLimit(strategy.getUsageLimit())
                .usedCount(strategy.getUsedCount())
                .status(strategy.getStatus())
                .createdBy(strategy.getCreatedBy())
                .build();

        // 解析目标城市列表
        detailVo.setTargetCitiesList(parseTargetCitiesList(strategy.getTargetCities()));

        // 计算剩余可用次数
        if (strategy.getUsageLimit() != null && strategy.getUsedCount() != null) {
            detailVo.setRemainingCount(strategy.getUsageLimit() - strategy.getUsedCount());
        }

        // 判断是否当前有效
        LocalDateTime now = LocalDateTime.now();
//        boolean isValid = AdminConstants.DiscountStrategyStatus.ACTIVE.equals(strategy.getStatus()) &&
//                         (strategy.getStartTime() == null || now.isAfter(strategy.getStartTime())) &&
//                         (strategy.getEndTime() == null || now.isBefore(strategy.getEndTime()));
        detailVo.setIsCurrentlyValid(true);

        return detailVo;
    }

    private String parseTargetCitiesDisplay(String targetCitiesJson) {
        if (targetCitiesJson == null || targetCitiesJson.trim().isEmpty()) {
            return "全国通用";
        }

        try {
            // 简单的JSON数组解析，实际项目中应使用JSON库
            String cities = targetCitiesJson.replaceAll("[\\[\\]\"\\s]", "");
            return cities.isEmpty() ? "全国通用" : cities.replace(",", ", ");
        } catch (Exception e) {
            logger.warn("解析目标城市失败：{}", targetCitiesJson);
            return "解析失败";
        }
    }

    private List<String> parseTargetCitiesList(String targetCitiesJson) {
        if (targetCitiesJson == null || targetCitiesJson.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // 简单的JSON数组解析，实际项目中应使用JSON库
            String cities = targetCitiesJson.replaceAll("[\\[\\]\"\\s]", "");
            if (cities.isEmpty()) {
                return new ArrayList<>();
            }
            return Arrays.asList(cities.split(","));
        } catch (Exception e) {
            logger.warn("解析目标城市列表失败：{}", targetCitiesJson);
            return new ArrayList<>();
        }
    }

    private int countByStatus(String status) {
        return Math.toIntExact(discountStrategyMapper.selectCount(
                Wrappers.<DiscountStrategy>lambdaQuery().eq(DiscountStrategy::getStatus, status)
        ));
    }

    private int countByStrategyType(String type) {
        return Math.toIntExact(discountStrategyMapper.selectCount(
                Wrappers.<DiscountStrategy>lambdaQuery().eq(DiscountStrategy::getStrategyType, type)
        ));
    }

    private boolean checkStrategyNameExists(String strategyName, Long excludeId) {
        int count = Math.toIntExact(discountStrategyMapper.selectCount(Wrappers.<DiscountStrategy>lambdaQuery()
                        .eq(DiscountStrategy::getStrategyName, strategyName)
                        .ne(DiscountStrategy::getId, excludeId)
        ));
        return count > 0;
    }
}

package org.software.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.software.code.dto.DiscountStrategyCreateDto;
import org.software.code.dto.DiscountStrategySearchDto;
import org.software.code.entity.DiscountStrategy;
import org.software.code.vo.DiscountStrategyDetailVo;
import org.software.code.vo.DiscountStrategyListVo;

import java.util.List;

/**
 * DiscountStrategyService 是折扣策略管理业务服务接口，定义了折扣优惠策略的全生命周期管理核心业务方法。
 * 
 * <p>本服务接口封装了折扣策略相关的所有业务逻辑，包括：</p>
 * <ul>
 *   <li>策略信息管理：策略增删改查、详情获取、配置维护</li>
 *   <li>策略状态控制：启用/禁用策略、有效期管理、批量操作</li>
 *   <li>策略分类管理：按类型分组、按城市筛选、按使用场景过滤</li>
 *   <li>策略使用统计：使用次数追踪、效果分析、收益统计</li>
 *   <li>策略验证功能：名称唯一性检查、配置合理性验证</li>
 * </ul>
 * 
 * <p>业务特性：</p>
 * <ul>
 *   <li>支持多种折扣类型：出行折扣、支付优惠、新用户福利、节假日活动</li>
 *   <li>提供灵活的策略配置：时间范围、适用城市、用户群体、使用条件</li>
 *   <li>实现策略冲突检测和优先级管理</li>
 *   <li>集成策略使用效果分析和ROI计算</li>
 *   <li>支持策略A/B测试和效果对比</li>
 * </ul>
 * 
 * <p>权限控制：</p>
 * <ul>
 *   <li>ADMIN角色：可查看、创建、编辑策略和状态管理</li>
 *   <li>SUPER_ADMIN角色：具有所有权限，包括策略删除和高级配置</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface DiscountStrategyService extends IService<DiscountStrategy> {
        /**
     * 分页查询折扣策略列表
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param searchDto  搜索条件
     * @return 折扣策略列表分页结果
     */
    Page<DiscountStrategyListVo> getDiscountStrategyPage(Integer pageNum, Integer pageSize, DiscountStrategySearchDto searchDto);

    /**
     * 根据ID获取折扣策略详细信息
     *
     * @param strategyId 策略ID
     * @return 折扣策略详细信息
     */
    DiscountStrategyDetailVo getDiscountStrategyDetail(Long strategyId);

    /**
     * 创建折扣策略
     *
     * @param createDto 创建信息
     * @param adminId   创建者管理员ID
     * @return 创建的策略详细信息，创建失败返回null
     */
    DiscountStrategyDetailVo createDiscountStrategy(DiscountStrategyCreateDto createDto, Long adminId);

    /**
     * 更新折扣策略
     *
     * @param strategyId 策略ID
     * @param createDto  更新信息
     * @param adminId    更新者管理员ID
     * @return 更新后的策略详细信息，更新失败返回null
     */
    DiscountStrategyDetailVo updateDiscountStrategy(Long strategyId, DiscountStrategyCreateDto createDto, Long adminId);

    /**
     * 删除折扣策略（仅超级管理员）
     *
     * @param strategyId 策略ID
     * @param adminId    操作管理员ID
     * @return 删除结果
     */
    Boolean deleteDiscountStrategy(Long strategyId, Long adminId);

    /**
     * 启用折扣策略
     *
     * @param strategyId 策略ID
     * @param adminId    操作管理员ID
     * @return 操作结果
     */
    Boolean enableDiscountStrategy(Long strategyId, Long adminId);

    /**
     * 禁用折扣策略
     *
     * @param strategyId 策略ID
     * @param adminId    操作管理员ID
     * @return 操作结果
     */
    Boolean disableDiscountStrategy(Long strategyId, Long adminId);

    /**
     * 根据策略类型获取活跃策略列表
     *
     * @param strategyType 策略类型
     * @return 策略列表
     */
    List<DiscountStrategyListVo> getActiveStrategiesByType(String strategyType);

    /**
     * 根据城市获取可用策略列表
     *
     * @param city 城市名称
     * @return 策略列表
     */
    List<DiscountStrategyListVo> getAvailableStrategiesByCity(String city);

    /**
     * 批量更新策略状态
     *
     * @param strategyIds 策略ID列表
     * @param status      新状态
     * @param adminId     操作管理员ID
     * @return 更新结果
     */
    Boolean batchUpdateStrategyStatus(List<Long> strategyIds, String status, Long adminId);


    /**
     * 获取策略统计信息
     *
     * @return 策略统计信息
     */
    DiscountStrategyStatisticsVo getDiscountStrategyStatistics();

    /**
     * 策略统计视图对象
     */
    class DiscountStrategyStatisticsVo {
        private Integer totalStrategies;
        private Integer activeStrategies;
        private Integer inactiveStrategies;
        private Integer expiredStrategies;
        private Integer travelStrategies;
        private Integer paymentStrategies;
        private Integer newUserStrategies;
        private Integer holidayStrategies;

        // Getters and Setters
        public Integer getTotalStrategies() {
            return totalStrategies;
        }

        public void setTotalStrategies(Integer totalStrategies) {
            this.totalStrategies = totalStrategies;
        }

        public Integer getActiveStrategies() {
            return activeStrategies;
        }

        public void setActiveStrategies(Integer activeStrategies) {
            this.activeStrategies = activeStrategies;
        }

        public Integer getInactiveStrategies() {
            return inactiveStrategies;
        }

        public void setInactiveStrategies(Integer inactiveStrategies) {
            this.inactiveStrategies = inactiveStrategies;
        }

        public Integer getExpiredStrategies() {
            return expiredStrategies;
        }

        public void setExpiredStrategies(Integer expiredStrategies) {
            this.expiredStrategies = expiredStrategies;
        }

        public Integer getTravelStrategies() {
            return travelStrategies;
        }

        public void setTravelStrategies(Integer travelStrategies) {
            this.travelStrategies = travelStrategies;
        }

        public Integer getPaymentStrategies() {
            return paymentStrategies;
        }

        public void setPaymentStrategies(Integer paymentStrategies) {
            this.paymentStrategies = paymentStrategies;
        }

        public Integer getNewUserStrategies() {
            return newUserStrategies;
        }

        public void setNewUserStrategies(Integer newUserStrategies) {
            this.newUserStrategies = newUserStrategies;
        }

        public Integer getHolidayStrategies() {
            return holidayStrategies;
        }

        public void setHolidayStrategies(Integer holidayStrategies) {
            this.holidayStrategies = holidayStrategies;
        }
    }
}

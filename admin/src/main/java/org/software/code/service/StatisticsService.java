package org.software.code.service;

import org.software.code.vo.DeviceStatisticsVo;
import org.software.code.vo.DiscountStatisticsVo;
import org.software.code.vo.UserStatisticsVo;

/**
 * StatisticsService 是数据统计分析业务服务接口，定义了系统各模块数据统计和分析报表的核心业务方法。
 * 
 * <p>本服务接口封装了统计分析相关的所有业务逻辑，包括：</p>
 * <ul>
 *   <li>用户数据统计：注册用户数、活跃用户数、用户增长趋势</li>
 *   <li>设备运营统计：设备使用率、故障率、维护成本、性能指标</li>
 *   <li>折扣策略效果：策略使用情况、成本效益、用户转化率</li>
 *   <li>综合运营报表：收入分析、成本控制、盈利能力评估</li>
 * </ul>
 * 
 * <p>统计特性：</p>
 * <ul>
 *   <li>支持多维度数据分析：时间维度、地域维度、用户群体维度</li>
 *   <li>提供实时和历史数据对比分析</li>
 *   <li>实现数据可视化图表生成</li>
 *   <li>支持自定义统计周期和条件筛选</li>
 *   <li>集成数据导出和报告生成功能</li>
 * </ul>
 * 
 * <p>应用场景：</p>
 * <ul>
 *   <li>管理决策支持：运营数据分析、业务发展趋势预测</li>
 *   <li>运营监控：实时监控关键指标、异常预警</li>
 *   <li>绩效评估：部门绩效、策略效果、投资回报评估</li>
 *   <li>用户洞察：用户行为分析、偏好挖掘、精准营销</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface StatisticsService {

    /**
     * 获取用户统计数据方法
     * 
     * <p>此方法用于获取指定时间范围内的用户相关统计数据，包括新增用户、活跃用户、
     * 用户分布等关键指标。</p>
     * 
     * <p>统计内容包括：</p>
     * <ul>
     *   <li>用户增长：新注册用户数、累计用户数、增长率</li>
     *   <li>用户活跃度：日活跃用户、月活跃用户、留存率</li>
     *   <li>用户分布：地域分布、年龄分布、注册渠道分布</li>
     *   <li>用户状态：正常用户、待审核用户、禁用用户比例</li>
     * </ul>
     *
     * @param startDate 统计开始日期，格式为yyyy-MM-dd
     * @param endDate 统计结束日期，格式为yyyy-MM-dd
     * @return 用户统计数据对象
     * @see UserStatisticsVo 用户统计数据结构说明
     */
    UserStatisticsVo getUserStatistics(String startDate, String endDate);

    /**
     * 获取设备统计数据方法
     * 
     * <p>此方法用于获取指定时间范围、城市和站点的设备运营统计数据，
     * 监控设备性能和维护需求。</p>
     * 
     * <p>统计内容包括：</p>
     * <ul>
     *   <li>设备状态：在线设备数、离线设备数、故障设备数</li>
     *   <li>设备使用：使用频次、使用时长、负载分布</li>
     *   <li>设备维护：维护次数、维护成本、故障类型分析</li>
     *   <li>设备性能：响应时间、成功率、异常率统计</li>
     * </ul>
     *
     * @param startDate 统计开始日期，格式为yyyy-MM-dd
     * @param endDate 统计结束日期，格式为yyyy-MM-dd
     * @param city 城市名称，可选，为空时统计所有城市
     * @param siteId 站点ID，可选，为空时统计该城市所有站点
     * @return 设备统计数据对象
     * @see DeviceStatisticsVo 设备统计数据结构说明
     */
    DeviceStatisticsVo getDeviceStatistics(String startDate, String endDate, String city, Long siteId);

    /**
     * 获取折扣策略统计数据方法
     * 
     * <p>此方法用于获取指定时间范围和策略类型的折扣策略使用统计，
     * 评估营销活动效果和投资回报率。</p>
     * 
     * <p>统计内容包括：</p>
     * <ul>
     *   <li>策略使用：使用次数、参与用户数、覆盖率</li>
     *   <li>策略效果：转化率、客单价提升、用户留存</li>
     *   <li>策略成本：折扣金额、营销成本、ROI计算</li>
     *   <li>策略对比：不同策略效果对比、A/B测试结果</li>
     * </ul>
     *
     * @param startDate 统计开始日期，格式为yyyy-MM-dd
     * @param endDate 统计结束日期，格式为yyyy-MM-dd
     * @param strategyType 策略类型，可选，为空时统计所有类型
     * @return 折扣策略统计数据对象
     * @see DiscountStatisticsVo 折扣统计数据结构说明
     */
    DiscountStatisticsVo getDiscountStatistics(String startDate, String endDate, String strategyType);
}
package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.annotation.AdminRole;
import org.software.code.common.result.Result;
import org.software.code.service.*;
import org.software.code.vo.DeviceStatisticsVo;
import org.software.code.vo.DiscountStatisticsVo;
import org.software.code.vo.TravelStatisticsVo;
import org.software.code.vo.UserStatisticsVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * StatisticsController 是数据统计分析控制器，提供系统各模块业务数据统计分析的REST API接口。
 * 
 * <p>本控制器负责处理所有统计相关的请求，包括：</p>
 * <ul>
 *   <li>用户数据统计：用户注册、审核、活跃度等统计分析</li>
 *   <li>出行数据统计：出行记录、扫码记录、路线分析等</li>
 *   <li>设备运营统计：设备状态、使用率、故障分析等</li>
 *   <li>折扣策略統計：策略使用效果、转化率、ROI分析等</li>
 *   <li>站点基础統計：站点分布、运营状态、设备配置等</li>
 * </ul>
 * 
 * <p>统计功能特性：</p>
 * <ul>
 *   <li>支持灵活的时间范围筛选和多维度数据分析</li>
 *   <li>提供简单统计和复杂统计两种模式</li>
 *   <li>集成数据可视化支持和报表导出功能</li>
 *   <li>实现实时数据监控和历史趋势分析</li>
 * </ul>
 * 
 * <p>权限控制：所有统计接口均需要管理员身份认证，部分高级统计需要SUPER_ADMIN权限。</p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Tag(name = "数据统计", description = "用户、出行、设备、折扣策略、站点等业务数据统计分析接口")
@RestController
@RequestMapping("/statistics")
@Validated
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Resource
    private StatisticsService statisticsService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private SiteService siteService;
    
    @Resource
    private TurnstileDeviceService deviceService;
    
    @Resource
    private DiscountStrategyService discountStrategyService;

    /**
     * 获取用户统计数据接口（基于时间范围）
     * 
     * <p>此接口通过StatisticsService获取指定时间范围内的用户统计数据，
     * 支持灵活的时间范围筛选和多维度分析。</p>
     * 
     * <p>统计内容包括：</p>
     * <ul>
     *   <li>用户增长趋势：新注册用户数、累计用户数、增长率</li>
     *   <li>用户活跃度：活跃用户数、登录频次、使用时长</li>
     *   <li>用户状态分布：待审核、已通过、已拒绝、已禁用用户数量</li>
     *   <li>用户地域分布：按城市、地区的用户分布情况</li>
     * </ul>
     *
     * @param startDate 统计开始日期，格式为yyyy-MM-dd，可选
     * @param endDate 统计结束日期，格式为yyyy-MM-dd，可选
     * @param adminId 当前管理员ID
     * @return 用户统计数据，包含各维度统计指标
     * @see UserStatisticsVo 用户统计数据结构说明
     */
    @GetMapping("/user")
    @Operation(summary = "用户统计数据", description = "获取用户注册、审核、状态等统计数据")
    @AdminRole()
    public Result<?> getUserStatistics(
            @Parameter(description = "统计开始日期 (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "统计结束日期 (yyyy-MM-dd)") @RequestParam(required = false) String endDate,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询用户统计数据，管理员ID：{}，开始日期：{}，结束日期：{}", adminId, startDate, endDate);
        
        UserStatisticsVo statistics = statisticsService.getUserStatistics(startDate, endDate);
        return Result.success(statistics);
    }


    /**
     * 获取出行统计数据接口
     * 
     * <p>此接口用于获取指定时间范围和城市的出行相关统计数据，
     * 分析用户出行行为和系统使用情况。</p>
     *
     * @param startDate 统计开始日期，格式为yyyy-MM-dd，可选
     * @param endDate 统计结束日期，格式为yyyy-MM-dd，可选
     * @param city 城市名称，可选，为空时统计所有城市
     * @param adminId 当前管理员ID
     * @return 出行统计数据
     * @see TravelStatisticsVo 出行统计数据结构说明
     */
    @GetMapping("/travel")
    @Operation(summary = "出行数据统计", description = "获取出行记录、扫码记录等统计数据")
    @AdminRole()
    public Result<?> getTravelStatistics(
            @Parameter(description = "统计开始日期 (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "统计结束日期 (yyyy-MM-dd)") @RequestParam(required = false) String endDate,
            @Parameter(description = "城市名称") @RequestParam(required = false) String city,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询出行统计数据，管理员ID：{}，开始日期：{}，结束日期：{}，城市：{}", adminId, startDate, endDate, city);
        
        TravelStatisticsVo statistics = statisticsService.getTravelStatistics(startDate, endDate, city);
        return Result.success(statistics);
    }

    /**
     * 获取设备统计数据接口（基于时间范围）
     * 
     * <p>此接口用于获取指定时间范围、城市和站点的设备运营统计数据，
     * 支持多维度筛选和深度分析。</p>
     *
     * @param startDate 统计开始日期，格式为yyyy-MM-dd，可选
     * @param endDate 统计结束日期，格式为yyyy-MM-dd，可选
     * @param city 城市名称，可选，为空时统计所有城市
     * @param siteId 站点ID，可选，为空时统计该城市所有站点
     * @param adminId 当前管理员ID
     * @return 设备统计数据
     * @see DeviceStatisticsVo 设备统计数据结构说明
     */
    @GetMapping("/device")
    @Operation(summary = "设备使用统计", description = "获取设备状态、使用频率等统计数据")
    @AdminRole()
    public Result<?> getDeviceStatistics(
            @Parameter(description = "统计开始日期 (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "统计结束日期 (yyyy-MM-dd)") @RequestParam(required = false) String endDate,
            @Parameter(description = "城市名称") @RequestParam(required = false) String city,
            @Parameter(description = "站点ID") @RequestParam(required = false) Long siteId,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询设备统计数据，管理员ID：{}，开始日期：{}，结束日期：{}，城市：{}，站点ID：{}", 
                    adminId, startDate, endDate, city, siteId);
        
        DeviceStatisticsVo statistics = statisticsService.getDeviceStatistics(startDate, endDate, city, siteId);
        return Result.success(statistics);
    }


    /**
     * 获取折扣策略统计数据接口（基于时间范围）
     * 
     * <p>此接口用于获取指定时间范围和策略类型的折扣策略使用统计，
     * 评估营销活动效果和投资回报率。</p>
     *
     * @param startDate 统计开始日期，格式为yyyy-MM-dd，可选
     * @param endDate 统计结束日期，格式为yyyy-MM-dd，可选
     * @param strategyType 策略类型，可选，为空时统计所有类型
     * @param adminId 当前管理员ID
     * @return 折扣策略统计数据
     * @see DiscountStatisticsVo 折扣统计数据结构说明
     */
    @GetMapping("/discount")
    @Operation(summary = "折扣策略统计", description = "获取折扣策略使用、效果等统计数据")
    @AdminRole()
    public Result<?> getDiscountStatistics(
            @Parameter(description = "统计开始日期 (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "统计结束日期 (yyyy-MM-dd)") @RequestParam(required = false) String endDate,
            @Parameter(description = "策略类型") @RequestParam(required = false) String strategyType,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询折扣策略统计数据，管理员ID：{}，开始日期：{}，结束日期：{}，策略类型：{}", 
                    adminId, startDate, endDate, strategyType);
        
        DiscountStatisticsVo statistics = statisticsService.getDiscountStatistics(startDate, endDate, strategyType);
        return Result.success(statistics);
    }


    /**
     * 获取站点统计数据接口
     * 
     * <p>此接口通过SiteService获取站点的基础统计信息，
     * 包括站点分布、运营状态、设备配置等数据。</p>
     * 
     * <p>统计内容包括：</p>
     * <ul>
     *   <li>站点总数：系统中配置的所有站点数量</li>
     *   <li>运营状态分布：正常运营、暂停服务、维护中的站点数量</li>
     *   <li>城市分布：各城市的站点数量分布</li>
     *   <li>线路分布：各地铁线路的站点数量统计</li>
     *   <li>设备配置：站点关联的闸机设备配置情况</li>
     * </ul>
     * 
     * <p>此接口为新增接口，补充站点维度的统计分析能力。</p>
     *
     * @param adminId 当前管理员ID
     * @return 站点统计数据
     */
    @GetMapping("/site")
    @Operation(summary = "站点统计数据", description = "获取站点分布、运营状态等统计信息")
    @AdminRole()
    public Result<?> getSiteStatistics(
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询站点统计数据，管理员ID：{}", adminId);
        
        SiteService.SiteStatisticsVo statistics = siteService.getSiteStatistics();
        return Result.success(statistics);
    }
}
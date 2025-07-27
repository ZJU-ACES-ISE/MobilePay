package org.software.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.annotation.AdminRole;
import org.software.code.common.result.Result;
import org.software.code.dto.DiscountStrategyCreateDto;
import org.software.code.dto.DiscountStrategySearchDto;
import org.software.code.service.DiscountStrategyService;
import org.software.code.vo.DiscountStrategyDetailVo;
import org.software.code.vo.DiscountStrategyListVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DiscountStrategyController 是折扣策略管理控制器，提供折扣策略管理相关的REST API接口
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Tag(name = "折扣策略管理", description = "折扣策略的创建、查询、修改、删除等管理接口")
@RestController
@RequestMapping("/discounts")
@Validated
public class DiscountStrategyController {

    private static final Logger logger = LoggerFactory.getLogger(DiscountStrategyController.class);

    @Resource
    private DiscountStrategyService discountStrategyService;

    /**
     * 分页获取折扣策略列表
     */
    @GetMapping
    @Operation(summary = "分页获取折扣策略列表", description = "根据条件分页查询折扣策略列表")
    @AdminRole()
    public Result<?> getDiscountStrategyPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索条件") @ModelAttribute DiscountStrategySearchDto searchDto,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询折扣策略列表，管理员ID：{}，页码：{}，页大小：{}，搜索条件：{}", adminId, pageNum, pageSize, searchDto);
        
        Page<DiscountStrategyListVo> strategyPage = discountStrategyService.getDiscountStrategyPage(pageNum, pageSize, searchDto);
        return Result.success(strategyPage);
    }

    /**
     * 获取折扣策略详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取折扣策略详情", description = "根据ID获取折扣策略详细信息")
    @AdminRole()
    public Result<?> getDiscountStrategyDetail(
            @Parameter(description = "策略ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询折扣策略详情，管理员ID：{}，策略ID：{}", adminId, id);
        
        DiscountStrategyDetailVo strategyDetail = discountStrategyService.getDiscountStrategyDetail(id);
        if (strategyDetail == null) {
            return Result.failed("折扣策略不存在");
        }
        return Result.success(strategyDetail);
    }

    /**
     * 创建折扣策略
     */
    @PostMapping
    @Operation(summary = "创建折扣策略", description = "创建新的折扣策略")
    @AdminRole()
    public Result<?> createDiscountStrategy(
            @Parameter(description = "策略创建信息") @RequestBody @Valid DiscountStrategyCreateDto createDto,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员创建折扣策略，管理员ID：{}，策略名称：{}", adminId, createDto.getStrategyName());
        
        DiscountStrategyDetailVo createdStrategy = discountStrategyService.createDiscountStrategy(createDto, Long.parseLong(adminId));
        if (createdStrategy != null) {
            return Result.success(createdStrategy);
        } else {
            return Result.failed("创建折扣策略失败");
        }
    }

    /**
     * 更新折扣策略
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新折扣策略", description = "更新指定折扣策略的信息")
    @AdminRole()
    public Result<?> updateDiscountStrategy(
            @Parameter(description = "策略ID") @PathVariable @NotNull Long id,
            @Parameter(description = "策略更新信息") @RequestBody @Valid DiscountStrategyCreateDto createDto,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员更新折扣策略，管理员ID：{}，策略ID：{}", adminId, id);
        
        DiscountStrategyDetailVo updatedStrategy = discountStrategyService.updateDiscountStrategy(id, createDto, Long.parseLong(adminId));
        if (updatedStrategy != null) {
            return Result.success(updatedStrategy);
        } else {
            return Result.failed("更新折扣策略失败");
        }
    }

    /**
     * 删除折扣策略（仅超级管理员）
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除折扣策略", description = "删除指定的折扣策略（仅超级管理员）")
    @AdminRole("SUPER_ADMIN")
    public Result<?> deleteDiscountStrategy(
            @Parameter(description = "策略ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("超级管理员删除折扣策略，管理员ID：{}，策略ID：{}", adminId, id);
        
        Boolean result = discountStrategyService.deleteDiscountStrategy(id, Long.parseLong(adminId));
        if (result) {
            return Result.success();
        } else {
            return Result.failed("删除折扣策略失败");
        }
    }

    /**
     * 启用折扣策略
     */
    @PostMapping("/{id}/enable")
    @Operation(summary = "启用折扣策略", description = "启用指定的折扣策略")
    @AdminRole()
    public Result<?> enableDiscountStrategy(
            @Parameter(description = "策略ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员启用折扣策略，管理员ID：{}，策略ID：{}", adminId, id);
        
        Boolean result = discountStrategyService.enableDiscountStrategy(id, Long.parseLong(adminId));
        if (result) {
            return Result.success();
        } else {
            return Result.failed("启用折扣策略失败");
        }
    }

    /**
     * 禁用折扣策略
     */
    @PostMapping("/{id}/disable")
    @Operation(summary = "禁用折扣策略", description = "禁用指定的折扣策略")
    @AdminRole()
    public Result<?> disableDiscountStrategy(
            @Parameter(description = "策略ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员禁用折扣策略，管理员ID：{}，策略ID：{}", adminId, id);
        
        Boolean result = discountStrategyService.disableDiscountStrategy(id, Long.parseLong(adminId));
        if (result) {
            return Result.success();
        } else {
            return Result.failed("禁用折扣策略失败");
        }
    }

    /**
     * 根据策略类型获取活跃策略列表
     */
    @GetMapping("/active/{strategyType}")
    @Operation(summary = "获取活跃策略列表", description = "根据策略类型获取当前活跃的策略列表")
    @AdminRole()
    public Result<?> getActiveStrategiesByType(
            @Parameter(description = "策略类型") @PathVariable @NotEmpty String strategyType,
            @RequestHeader("X-User-Id") String adminId, @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询活跃策略列表，管理员ID：{}，策略类型：{}", adminId, strategyType);
        
        List<DiscountStrategyListVo> strategies = discountStrategyService.getActiveStrategiesByType(strategyType);
        return Result.success(strategies);
    }

    /**
     * 根据城市获取可用策略列表
     */
    @GetMapping("/city-available")
    @Operation(summary = "获取城市可用策略", description = "根据城市名称获取可用的策略列表")
    @AdminRole()
    public Result<?> getAvailableStrategiesByCity(
            @Parameter(description = "城市名称", required = true) @RequestParam @NotEmpty String city,
            @RequestHeader("X-User-Id") String adminId, @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询城市可用策略，管理员ID：{}，城市：{}", adminId, city);
        
        List<DiscountStrategyListVo> strategies = discountStrategyService.getAvailableStrategiesByCity(city);
        return Result.success(strategies);
    }

    /**
     * 批量更新策略状态
     */
    @PostMapping("/batch/status")
    @Operation(summary = "批量更新策略状态", description = "批量更新多个策略的状态")
    @AdminRole()
    public Result<?> batchUpdateStrategyStatus(
            @Parameter(description = "策略ID列表") @RequestParam @NotEmpty List<Long> strategyIds,
            @Parameter(description = "新状态") @RequestParam @NotEmpty String status,
            @RequestHeader("X-User-Id") String adminId, @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员批量更新策略状态，管理员ID：{}，策略数量：{}，新状态：{}", adminId, strategyIds.size(), status);
        
        Boolean result = discountStrategyService.batchUpdateStrategyStatus(strategyIds, status, Long.parseLong(adminId));
        if (result) {
            return Result.success();
        } else {
            return Result.failed("批量更新策略状态失败");
        }
    }
}
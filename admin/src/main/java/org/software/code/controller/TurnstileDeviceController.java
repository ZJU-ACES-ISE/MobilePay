package org.software.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.annotation.AdminRole;
import org.software.code.common.result.Result;
import org.software.code.dto.DeviceCreateDto;
import org.software.code.dto.DeviceSearchDto;
import org.software.code.dto.DeviceUpdateDto;
import org.software.code.service.TurnstileDeviceService;
import org.software.code.vo.DeviceDetailVo;
import org.software.code.vo.DeviceListVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TurnstileDeviceController 是闸机设备控制器
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@RestController
@RequestMapping("/devices")
@Tag(name = "闸机设备管理", description = "闸机设备的增删改查和状态管理")
@Validated
public class TurnstileDeviceController {

    private static final Logger logger = LoggerFactory.getLogger(TurnstileDeviceController.class);

    @Resource
    private TurnstileDeviceService deviceService;

    /**
     * 分页获取设备列表
     */
    @GetMapping
    @Operation(summary = "分页获取设备列表", description = "根据条件分页查询设备列表")
    @AdminRole()
    public Result<?> getDevicePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索条件") @ModelAttribute DeviceSearchDto searchDto,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询设备列表，管理员ID：{}，页码：{}，页大小：{}", adminId, pageNum, pageSize);
        
        Page<DeviceListVo> devicePage = deviceService.getDevicePage(pageNum, pageSize, searchDto);
        return Result.success(devicePage);
    }

    /**
     * 获取设备详情
     */
    @GetMapping("/{deviceId}")
    @Operation(summary = "获取设备详情", description = "根据ID获取设备详细信息")
    @AdminRole()
    public Result<?> getDeviceDetail(
            @Parameter(description = "设备ID") @PathVariable @NotNull Long deviceId,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询设备详情，管理员ID：{}，设备ID：{}", adminId, deviceId);
        
        DeviceDetailVo deviceDetail = deviceService.getDeviceDetail(deviceId);
        if (deviceDetail == null) {
            return Result.failed("设备不存在");
        }
        return Result.success(deviceDetail);
    }

    /**
     * 创建设备
     */
    @PostMapping
    @Operation(summary = "创建设备", description = "创建新的设备")
    @AdminRole("SUPER_ADMIN")
    public Result<?> createDevice(
            @Parameter(description = "设备创建信息") @RequestBody @Valid DeviceCreateDto createDto,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员创建设备，管理员ID：{}，设备编码：{}", adminId, createDto.getDeviceCode());
        
        DeviceDetailVo result = deviceService.createDevice(createDto, Long.parseLong(adminId));
        if (result != null) {
            return Result.success(result);
        } else {
            return Result.failed("创建设备失败");
        }
    }

    /**
     * 更新设备信息
     */
    @PutMapping("/{deviceId}")
    @Operation(summary = "更新设备信息", description = "更新指定设备的信息")
    @AdminRole("SUPER_ADMIN")
    public Result<?> updateDevice(
            @Parameter(description = "设备ID") @PathVariable @NotNull Long deviceId,
            @Parameter(description = "设备更新信息") @RequestBody @Valid DeviceUpdateDto updateDto,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员更新设备信息，管理员ID：{}，设备ID：{}", adminId, deviceId);
        
        DeviceDetailVo result = deviceService.updateDevice(deviceId, updateDto, Long.parseLong(adminId));
        if (result != null) {
            return Result.success(result);
        } else {
            return Result.failed("更新设备信息失败");
        }
    }

    /**
     * 删除设备
     */
    @DeleteMapping("/{deviceId}")
    @Operation(summary = "删除设备", description = "删除指定的设备")
    @AdminRole("SUPER_ADMIN")
    public Result<?> deleteDevice(
            @Parameter(description = "设备ID") @PathVariable @NotNull Long deviceId,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员删除设备，管理员ID：{}，设备ID：{}", adminId, deviceId);
        
        Boolean result = deviceService.deleteDevice(deviceId, Long.parseLong(adminId));
        if (result) {
            return Result.success();
        } else {
            return Result.failed("删除设备失败");
        }
    }


    /**
     * 批量更新设备状态
     */
    @PostMapping("/batch/status")
    @Operation(summary = "批量更新设备状态", description = "批量更新多个设备的状态")
    @AdminRole()
    public Result<?> batchUpdateDeviceStatus(
            @Parameter(description = "设备ID列表") @RequestParam @NotEmpty List<Long> deviceIds,
            @Parameter(description = "新状态") @RequestParam @NotEmpty String status,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员批量更新设备状态，管理员ID：{}，设备数量：{}，新状态：{}", adminId, deviceIds.size(), status);
        
        Boolean result = deviceService.batchUpdateDeviceStatus(deviceIds, status, Long.parseLong(adminId));
        if (result) {
            return Result.success();
        } else {
            return Result.failed("批量更新设备状态失败");
        }
    }

    /**
     * 更新设备心跳
     */
    @PostMapping("/heartbeat")
    @Operation(summary = "更新设备心跳", description = "更新设备的心跳时间")
    public Result<?> updateDeviceHeartbeat(
            @Parameter(description = "设备编码") @RequestParam @NotEmpty String deviceCode) {
        
        logger.debug("设备心跳更新，设备编码：{}", deviceCode);
        
        Boolean result = deviceService.updateDeviceHeartbeat(deviceCode, LocalDateTime.now());
        if (result) {
            return Result.success();
        } else {
            return Result.failed("更新设备心跳失败");
        }
    }

    /**
     * 根据关键字搜索设备
     */
    @GetMapping("/search")
    @Operation(summary = "搜索设备", description = "根据关键字搜索设备")
    @AdminRole()
    public Result<?> searchDevices(
            @Parameter(description = "搜索关键字") @RequestParam @NotEmpty String keyword,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员搜索设备，管理员ID：{}，关键字：{}", adminId, keyword);
        
        List<DeviceListVo> devices = deviceService.searchDevicesByKeyword(keyword);
        return Result.success(devices);
    }
}
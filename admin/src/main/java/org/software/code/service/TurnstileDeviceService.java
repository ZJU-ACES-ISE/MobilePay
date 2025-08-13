package org.software.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.software.code.dto.DeviceCreateDto;
import org.software.code.dto.DeviceSearchDto;
import org.software.code.dto.DeviceUpdateDto;
import org.software.code.entity.TurnstileDevice;
import org.software.code.vo.DeviceDetailVo;
import org.software.code.vo.DeviceListVo;
import org.software.code.vo.DeviceStatisticsVo;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TurnstileDeviceService 是闸机设备管理业务服务接口，定义了地铁闸机设备的全生命周期管理核心业务方法。
 * 
 * <p>本服务接口封装了闸机设备相关的所有业务逻辑，包括：</p>
 * <ul>
 *   <li>设备信息管理：设备增删改查、详情获取、配置更新</li>
 *   <li>设备状态监控：在线状态检测、心跳包处理、故障诊断</li>
 *   <li>设备运维管理：批量操作、状态变更、维护记录</li>
 *   <li>设备搜索功能：关键字搜索、多条件筛选、位置查询</li>
 *   <li>统计分析功能：设备统计、运行状态分析、性能监控</li>
 * </ul>
 * 
 * <p>业务特性：</p>
 * <ul>
 *   <li>实时监控设备在线状态和运行健康度</li>
 *   <li>支持设备批量配置和状态批量变更</li>
 *   <li>提供设备故障预警和维护提醒机制</li>
 *   <li>集成设备使用统计和性能分析</li>
 *   <li>支持设备远程控制和配置下发</li>
 * </ul>
 * 
 * <p>权限控制：</p>
 * <ul>
 *   <li>ADMIN角色：可查看、监控设备状态和基本配置</li>
 *   <li>SUPER_ADMIN角色：具有所有权限，包括设备创建、删除和高级配置</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface TurnstileDeviceService extends IService<TurnstileDevice> {

    Page<DeviceListVo> getDevicePage(Integer pageNum, Integer pageSize, DeviceSearchDto searchDto);

    Boolean updateDeviceHeartbeat(@NotEmpty String deviceCode, LocalDateTime now);

    List<DeviceListVo> searchDevicesByKeyword(@NotEmpty String keyword);

    Boolean batchUpdateDeviceStatus(@NotEmpty List<Long> deviceIds, @NotEmpty String status, Long adminId);

    Boolean deleteDevice(@NotNull Long deviceId, Long adminId);

    DeviceDetailVo updateDevice(@NotNull Long deviceId, @Valid DeviceUpdateDto updateDto, Long adminId);

    DeviceDetailVo createDevice(@Valid DeviceCreateDto createDto, Long adminId);

    DeviceDetailVo getDeviceDetail(@NotNull Long deviceId);
}

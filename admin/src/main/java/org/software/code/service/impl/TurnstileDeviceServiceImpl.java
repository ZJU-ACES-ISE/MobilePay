package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.consts.AdminConstants;
import org.software.code.dto.DeviceCreateDto;
import org.software.code.dto.DeviceSearchDto;
import org.software.code.dto.DeviceUpdateDto;
import org.software.code.entity.Site;
import org.software.code.entity.TurnstileDevice;
import org.software.code.mapper.SiteMapper;
import org.software.code.mapper.TurnstileDeviceMapper;
import org.software.code.service.TurnstileDeviceService;
import org.software.code.vo.DeviceDetailVo;
import org.software.code.vo.DeviceListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
@Service
public class TurnstileDeviceServiceImpl extends ServiceImpl<TurnstileDeviceMapper, TurnstileDevice> implements TurnstileDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(TurnstileDeviceServiceImpl.class);
    private static final int DEFAULT_HEARTBEAT_TIMEOUT_MINUTES = 5;

    @Resource
    private TurnstileDeviceMapper turnstileDeviceMapper;
    
    @Resource
    private SiteMapper siteMapper;


    @Override
    public Page<DeviceListVo> getDevicePage(Integer pageNum, Integer pageSize, DeviceSearchDto searchDto) {
        logger.info("获取设备列表分页数据，页码：{}，页大小：{}", pageNum, pageSize);

        LambdaQueryWrapper<TurnstileDevice> wrapper = Wrappers.<TurnstileDevice>lambdaQuery();
        
        // 添加空安全检查和所有搜索条件
        if (searchDto != null) {
            // 关键字搜索（设备名称或编码）
            if (StringUtils.hasText(searchDto.getKeyword())) {
                wrapper.and(w -> w.like(TurnstileDevice::getDeviceName, searchDto.getKeyword())
                               .or()
                               .like(TurnstileDevice::getDeviceCode, searchDto.getKeyword()));
            }
            
            // 站点ID过滤
            if (searchDto.getSiteId() != null) {
                wrapper.eq(TurnstileDevice::getSiteId, searchDto.getSiteId());
            }
            
            // 设备状态过滤
            if (StringUtils.hasText(searchDto.getStatus())) {
                wrapper.eq(TurnstileDevice::getStatus, searchDto.getStatus());
            }
            
            // 设备类型过滤
            if (StringUtils.hasText(searchDto.getDeviceType())) {
                wrapper.eq(TurnstileDevice::getDeviceType, searchDto.getDeviceType());
            }
            
            // 固件版本过滤
            if (StringUtils.hasText(searchDto.getFirmwareVersion())) {
                wrapper.eq(TurnstileDevice::getFirmwareVersion, searchDto.getFirmwareVersion());
            }
            
            // 创建时间范围过滤
            if (searchDto.getStartTime() != null) {
                wrapper.ge(TurnstileDevice::getCreatedTime, Date.from(searchDto.getStartTime().atZone(ZoneId.systemDefault()).toInstant()));
            }
            if (searchDto.getEndTime() != null) {
                wrapper.le(TurnstileDevice::getCreatedTime, Date.from(searchDto.getEndTime().atZone(ZoneId.systemDefault()).toInstant()));
            }
            
            // 心跳时间范围过滤
            if (searchDto.getHeartbeatStartTime() != null) {
                wrapper.ge(TurnstileDevice::getLastHeartbeat, Date.from(searchDto.getHeartbeatStartTime().atZone(ZoneId.systemDefault()).toInstant()));
            }
            if (searchDto.getHeartbeatEndTime() != null) {
                wrapper.le(TurnstileDevice::getLastHeartbeat, Date.from(searchDto.getHeartbeatEndTime().atZone(ZoneId.systemDefault()).toInstant()));
            }
            
            // 在线状态过滤
            if (searchDto.getIsOnline() != null) {
                int timeoutMinutes = searchDto.getHeartbeatTimeoutMinutes() != null ? 
                    searchDto.getHeartbeatTimeoutMinutes() : DEFAULT_HEARTBEAT_TIMEOUT_MINUTES;
                
                LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(timeoutMinutes);
                Date thresholdDate = Date.from(thresholdTime.atZone(ZoneId.systemDefault()).toInstant());
                
                if (searchDto.getIsOnline()) {
                    // 在线设备：心跳时间在阈值时间之后
                    wrapper.gt(TurnstileDevice::getLastHeartbeat, thresholdDate);
                } else {
                    // 离线设备：心跳时间在阈值时间之前或为空
                    wrapper.and(w -> w.lt(TurnstileDevice::getLastHeartbeat, thresholdDate)
                                   .or()
                                   .isNull(TurnstileDevice::getLastHeartbeat));
                }
            }
        }
        
        // 默认排序：按创建时间倒序
        wrapper.orderByDesc(TurnstileDevice::getCreatedTime);

        Page<TurnstileDevice> resultPage = turnstileDeviceMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        Page<DeviceListVo> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");

        List<DeviceListVo> deviceListVos = resultPage.getRecords().stream()
                .map(this::convertToDeviceListVo)
                .collect(Collectors.toList());

        voPage.setRecords(deviceListVos);

        return voPage;
    }

    @Override
    @Transactional
    public Boolean deleteDevice(Long deviceId, Long adminId) {
        logger.info("删除设备，设备ID：{}", deviceId);

        try {
            TurnstileDevice device = turnstileDeviceMapper.selectById(deviceId);
            if (device == null) {
                logger.warn("设备不存在，设备ID：{}", deviceId);
                return false;
            }

            int deleteCount = turnstileDeviceMapper.deleteById(deviceId);

            if (deleteCount > 0) {
                logger.info("删除设备成功，设备ID：{}", deviceId);
                return true;
            } else {
                logger.warn("删除设备失败，删除操作影响行数为0，设备ID：{}", deviceId);
                return false;
            }

        } catch (Exception e) {
            logger.error("删除设备失败，设备ID：{}，错误：{}", deviceId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean updateDeviceHeartbeat(String deviceCode, LocalDateTime now) {
        logger.info("更新设备心跳，设备编码：{}，时间：{}", deviceCode, now);

        try {
            TurnstileDevice device = turnstileDeviceMapper.selectOne(
                Wrappers.<TurnstileDevice>lambdaQuery()
                    .eq(TurnstileDevice::getDeviceCode, deviceCode)
            );

            if (device == null) {
                logger.warn("设备不存在，设备编码：{}", deviceCode);
                return false;
            }

            device.setLastHeartbeat(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
            device.setUpdatedTime(new Date());

            int updateCount = turnstileDeviceMapper.updateById(device);

            if (updateCount > 0) {
                logger.info("更新设备心跳成功，设备编码：{}", deviceCode);
                return true;
            } else {
                logger.warn("更新设备心跳失败，设备编码：{}", deviceCode);
                return false;
            }

        } catch (Exception e) {
            logger.error("更新设备心跳失败，设备编码：{}，错误：{}", deviceCode, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<DeviceListVo> searchDevicesByKeyword(String keyword) {
        logger.info("根据关键字搜索设备，关键字：{}", keyword);

        List<TurnstileDevice> devices = turnstileDeviceMapper.selectList(
            Wrappers.<TurnstileDevice>lambdaQuery()
                .and(w -> w.like(TurnstileDevice::getDeviceName, keyword)
                         .or()
                         .like(TurnstileDevice::getDeviceCode, keyword))
                .orderByDesc(TurnstileDevice::getCreatedTime)
        );

        return devices.stream()
                .map(this::convertToDeviceListVo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean batchUpdateDeviceStatus(List<Long> deviceIds, String status, Long adminId) {
        logger.info("批量更新设备状态，设备数量：{}，新状态：{}", deviceIds.size(), status);

        try {
            int updateCount = turnstileDeviceMapper.update(
                null,
                Wrappers.<TurnstileDevice>lambdaUpdate()
                    .set(TurnstileDevice::getStatus, status)
                    .set(TurnstileDevice::getUpdatedTime, new Date())
                    .in(TurnstileDevice::getId, deviceIds)
            );

            if (updateCount > 0) {
                logger.info("批量更新设备状态成功，影响行数：{}", updateCount);
                return true;
            } else {
                logger.warn("批量更新设备状态失败，影响行数为0");
                return false;
            }

        } catch (Exception e) {
            logger.error("批量更新设备状态失败，错误：{}", e.getMessage(), e);
            return false;
        }
    }


    @Override
    public DeviceDetailVo getDeviceDetail(Long deviceId) {
        logger.info("获取设备详细信息，设备ID：{}", deviceId);

        TurnstileDevice device = turnstileDeviceMapper.selectById(deviceId);
        if (device == null) {
            logger.warn("设备不存在，设备ID：{}", deviceId);
            return null;
        }

        return convertToDeviceDetailVo(device);
    }

    @Override
    @Transactional
    public DeviceDetailVo createDevice(DeviceCreateDto createDto, Long adminId) {
        logger.info("创建设备，设备编码：{}，设备名称：{}", createDto.getDeviceCode(), createDto.getDeviceName());

        try {
            // 检查设备编码是否已存在
            if (checkDeviceCodeExists(createDto.getDeviceCode(), null)) {
                logger.warn("设备编码已存在：{}", createDto.getDeviceCode());
                return null;
            }

            TurnstileDevice device = new TurnstileDevice();
            BeanUtils.copyProperties(createDto, device);
            device.setStatus(AdminConstants.DeviceStatus.ONLINE);
            device.setCreatedTime(new Date());
            device.setUpdatedTime(new Date());

            int insertCount = turnstileDeviceMapper.insert(device);
            
            if (insertCount > 0) {
                TurnstileDevice createdDevice = turnstileDeviceMapper.selectById(device.getId());
                return convertToDeviceDetailVo(createdDevice);
            } else {
                logger.warn("创建设备失败，插入操作影响行数为0");
                return null;
            }

        } catch (Exception e) {
            logger.error("创建设备失败，错误：{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public DeviceDetailVo updateDevice(Long deviceId, DeviceUpdateDto updateDto, Long adminId) {
        logger.info("更新设备信息，设备ID：{}", deviceId);

        try {
            TurnstileDevice existingDevice = turnstileDeviceMapper.selectById(deviceId);
            if (existingDevice == null) {
                logger.warn("设备不存在，设备ID：{}", deviceId);
                return null;
            }

            // 检查站点是否存在
            if (updateDto.getSiteId() != null) {
                Site site = siteMapper.selectById(updateDto.getSiteId());
                if (site == null) {
                    logger.warn("站点不存在，站点ID：{}", updateDto.getSiteId());
                    return null;
                }
            }

            TurnstileDevice device = new TurnstileDevice();
            BeanUtils.copyProperties(updateDto, device);
            device.setId(deviceId);
            device.setUpdatedTime(new Date());

            int updateCount = turnstileDeviceMapper.updateById(device);

            if (updateCount > 0) {
                logger.info("更新设备信息成功，设备ID：{}", deviceId);
                // 查询并返回更新后的设备详情
                TurnstileDevice updatedDevice = turnstileDeviceMapper.selectById(deviceId);
                return convertToDeviceDetailVo(updatedDevice);
            } else {
                logger.warn("更新设备信息失败，更新操作影响行数为0，设备ID：{}", deviceId);
                return null;
            }

        } catch (Exception e) {
            logger.error("更新设备信息失败，设备ID：{}，错误：{}", deviceId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将TurnstileDevice实体转换为DeviceListVo
     */
    private DeviceListVo convertToDeviceListVo(TurnstileDevice device) {
        DeviceListVo vo = new DeviceListVo();
        
        // 基本信息
        vo.setDeviceId(device.getId());
        vo.setDeviceCode(device.getDeviceCode());
        vo.setDeviceName(device.getDeviceName());
        vo.setSiteId(device.getSiteId());
        vo.setDeviceType(device.getDeviceType());
        vo.setStatus(device.getStatus());
        vo.setFirmwareVersion(device.getFirmwareVersion());
        
        // 时间转换
        if (device.getCreatedTime() != null) {
            vo.setCreatedTime(device.getCreatedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        if (device.getUpdatedTime() != null) {
            vo.setUpdatedTime(device.getUpdatedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        if (device.getLastHeartbeat() != null) {
            vo.setLastHeartbeat(device.getLastHeartbeat().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        
        // 计算在线状态
        vo.setIsOnline(calculateIsOnline(device.getLastHeartbeat()));
        
        // 获取站点信息
        if (device.getSiteId() != null) {
            Site site = siteMapper.selectById(device.getSiteId());
            if (site != null) {
                vo.setSiteName(site.getSiteName());
                vo.setSiteCode(site.getSiteCode());
            }
        }
        
        return vo;
    }

    /**
     * 将TurnstileDevice实体转换为DeviceDetailVo
     */
    private DeviceDetailVo convertToDeviceDetailVo(TurnstileDevice device) {
        DeviceDetailVo vo = new DeviceDetailVo();
        
        // 复制基本信息
        BeanUtils.copyProperties(device, vo);
        vo.setDeviceId(device.getId());
        
        // 时间转换
        if (device.getCreatedTime() != null) {
            vo.setCreatedTime(device.getCreatedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        if (device.getUpdatedTime() != null) {
            vo.setUpdatedTime(device.getUpdatedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        if (device.getLastHeartbeat() != null) {
            vo.setLastHeartbeat(device.getLastHeartbeat().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        
        // 计算在线状态
        vo.setIsOnline(calculateIsOnline(device.getLastHeartbeat()));
        
        // 获取站点详细信息
        if (device.getSiteId() != null) {
            Site site = siteMapper.selectById(device.getSiteId());
            if (site != null) {
                DeviceDetailVo.SiteInfo siteInfo = new DeviceDetailVo.SiteInfo();
                siteInfo.setSiteId(site.getId());
                siteInfo.setSiteName(site.getSiteName());
                siteInfo.setSiteCode(site.getSiteCode());
                // 只设置 SiteInfo 中存在的属性
                vo.setSiteInfo(siteInfo);
            }
        }
        
        return vo;
    }

    /**
     * 计算设备是否在线
     */
    private Boolean calculateIsOnline(Date lastHeartbeat) {
        if (lastHeartbeat == null) {
            return false;
        }
        
        LocalDateTime heartbeatTime = lastHeartbeat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(DEFAULT_HEARTBEAT_TIMEOUT_MINUTES);
        
        return heartbeatTime.isAfter(thresholdTime);
    }

    /**
     * 检查设备编码是否已存在
     */
    private Boolean checkDeviceCodeExists(String deviceCode, Long excludeId) {
        LambdaQueryWrapper<TurnstileDevice> wrapper = Wrappers.<TurnstileDevice>lambdaQuery()
                .eq(TurnstileDevice::getDeviceCode, deviceCode);
        
        if (excludeId != null) {
            wrapper.ne(TurnstileDevice::getId, excludeId);
        }
        
        Long count = turnstileDeviceMapper.selectCount(wrapper);
        return count > 0;
    }
}

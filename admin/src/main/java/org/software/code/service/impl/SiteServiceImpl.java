package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.consts.AdminConstants;
import org.software.code.dto.SiteCreateDto;
import org.software.code.dto.SiteSearchDto;
import org.software.code.dto.SiteUpdateDto;
import org.software.code.entity.Site;
import org.software.code.entity.TurnstileDevice;
import org.software.code.entity.TransitRecord;
import org.software.code.mapper.SiteMapper;
import org.software.code.mapper.TurnstileDeviceMapper;
import org.software.code.mapper.TransitRecordMapper;
import org.software.code.service.SiteService;
import org.software.code.vo.SiteDetailVo;
import org.software.code.vo.SiteListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class SiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements SiteService {
    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private TurnstileDeviceMapper turnstileDeviceMapper;

    @Resource
    private TransitRecordMapper transitRecordMapper;

    @Override
    public Page<SiteListVo> getSitePage(Integer pageNum, Integer pageSize, SiteSearchDto searchDto) {
        logger.info("获取站点列表分页数据，页码：{}，页大小：{}", pageNum, pageSize);

        LambdaQueryWrapper<Site> wrapper = Wrappers.<Site>lambdaQuery();
        
        // 添加空安全检查和所有搜索条件
        if (searchDto != null) {
            // 关键字搜索（站点名称或编码）
            if (StringUtils.hasText(searchDto.getKeyword())) {
                wrapper.and(w -> w.like(Site::getSiteName, searchDto.getKeyword())
                               .or()
                               .like(Site::getSiteCode, searchDto.getKeyword()));
            }
            
            // 状态过滤
            if (StringUtils.hasText(searchDto.getStatus())) {
                wrapper.eq(Site::getStatus, searchDto.getStatus());
            }
            
            // 城市过滤
            if (StringUtils.hasText(searchDto.getCity())) {
                wrapper.eq(Site::getCity, searchDto.getCity());
            }
            
            // 线路名称过滤
            if (StringUtils.hasText(searchDto.getLineName())) {
                wrapper.eq(Site::getLineName, searchDto.getLineName());
            }
            
            // 交通类型过滤
            if (StringUtils.hasText(searchDto.getType())) {
                wrapper.eq(Site::getType, searchDto.getType());
            }
            
            // 创建时间范围过滤
            if (searchDto.getStartTime() != null) {
                wrapper.ge(Site::getCreatedTime, java.sql.Timestamp.valueOf(searchDto.getStartTime()));
            }
            if (searchDto.getEndTime() != null) {
                wrapper.le(Site::getCreatedTime, java.sql.Timestamp.valueOf(searchDto.getEndTime()));
            }
            
            // 地理坐标范围过滤
            if (searchDto.getMinLongitude() != null && searchDto.getMaxLongitude() != null) {
                wrapper.between(Site::getLongitude, searchDto.getMinLongitude(), searchDto.getMaxLongitude());
            }
            if (searchDto.getMinLatitude() != null && searchDto.getMaxLatitude() != null) {
                wrapper.between(Site::getLatitude, searchDto.getMinLatitude(), searchDto.getMaxLatitude());
            }
        }
        
        // 默认排序：按创建时间倒序
        wrapper.orderByDesc(Site::getCreatedTime);

        Page<Site> resultPage = siteMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        Page<SiteListVo> voPage = new Page<>();
        BeanUtils.copyProperties(resultPage, voPage, "records");

        List<SiteListVo> siteListVos = resultPage.getRecords().stream()
            .map(this::convertToSiteListVo)
            .collect(Collectors.toList());

        voPage.setRecords(siteListVos);

        return voPage;
    }

    @Override
    public SiteDetailVo getSiteDetail(Long siteId) {
        logger.info("获取站点详细信息，站点ID：{}", siteId);

        Site site = siteMapper.selectById(siteId);
        if (site == null) {
            logger.warn("站点不存在，站点ID：{}", siteId);
            return null;
        }

        SiteDetailVo siteDetailVo = convertToSiteDetailVo(site);

        // 查询关联的设备信息
        List<TurnstileDevice> devices = turnstileDeviceMapper.selectList(
            Wrappers.<TurnstileDevice>lambdaQuery().eq(TurnstileDevice::getSiteId, siteId)
        );
        siteDetailVo.setDevices(convertToDeviceSimpleVos(devices));

        // 查询客流量统计信息
        SiteDetailVo.PassengerFlowStats flowStats = calculatePassengerFlowStats(siteId);
        siteDetailVo.setPassengerFlowStats(flowStats);

        return siteDetailVo;
    }

    @Override
    @Transactional
    public SiteDetailVo createSite(SiteCreateDto createDto, Long adminId) {
        logger.info("创建站点，站点编码：{}，站点名称：{}", createDto.getSiteCode(), createDto.getSiteName());

        try {
            // 检查站点编码是否已存在
            if (checkSiteCodeExists(createDto.getSiteCode(), null)) {
                logger.warn("站点编码已存在：{}", createDto.getSiteCode());
                return null;
            }

            // 检查站点名称是否已存在
            if (checkSiteNameExists(createDto.getSiteName(), null)) {
                logger.warn("站点名称已存在：{}", createDto.getSiteName());
                return null;
            }

            Site site = new Site();
            BeanUtils.copyProperties(createDto, site);
            site.setStatus(AdminConstants.SiteStatus.ACTIVE);
            site.setCreatedTime(LocalDateTime.now());
            site.setUpdatedTime(LocalDateTime.now());

            int insertCount = siteMapper.insert(site);

            if (insertCount > 0) {
                logger.info("创建站点成功，站点ID：{}", site.getId());
                // 返回创建的站点详细信息
                return getSiteDetail(site.getId());
            } else {
                logger.warn("创建站点失败，插入操作影响行数为0");
                return null;
            }

        } catch (Exception e) {
            logger.error("创建站点失败，错误：{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public SiteDetailVo updateSite(Long siteId, SiteUpdateDto updateDto, Long adminId) {
        logger.info("更新站点信息，站点ID：{}", siteId);

        try {
            Site existingSite = siteMapper.selectById(siteId);
            if (existingSite == null) {
                logger.warn("站点不存在，站点ID：{}", siteId);
                return null;
            }

            // 检查站点名称是否已存在（排除当前站点）
            if (updateDto.getSiteName() != null &&
                checkSiteNameExists(updateDto.getSiteName(), siteId)) {
                logger.warn("站点名称已存在：{}", updateDto.getSiteName());
                return null;
            }

            Site site = new Site();
            BeanUtils.copyProperties(updateDto, site);
            site.setId(siteId);
            site.setUpdatedTime(LocalDateTime.now());

            int updateCount = siteMapper.updateById(site);

            if (updateCount > 0) {
                logger.info("更新站点信息成功，站点ID：{}", siteId);
                // 返回更新后的站点详细信息
                return getSiteDetail(siteId);
            } else {
                logger.warn("更新站点信息失败，更新操作影响行数为0，站点ID：{}", siteId);
                return null;
            }

        } catch (Exception e) {
            logger.error("更新站点信息失败，站点ID：{}，错误：{}", siteId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean deleteSite(Long siteId, Long adminId) {
        logger.info("删除站点，站点ID：{}", siteId);

        try {
            Site site = siteMapper.selectById(siteId);
            if (site == null) {
                logger.warn("站点不存在，站点ID：{}", siteId);
                return false;
            }

            site.setUpdatedTime(LocalDateTime.now());

            int updateCount = siteMapper.deleteById(siteId);

            logger.info("删除站点成功，站点ID：{}", siteId);
            return updateCount > 0;

        } catch (Exception e) {
            logger.error("删除站点失败，站点ID：{}，错误：{}", siteId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean batchDeleteSites(List<Long> siteIds, Long adminId) {
        logger.info("批量删除站点，站点数量：{}", siteIds.size());

        try {
            for (Long siteId : siteIds) {
                deleteSite(siteId, adminId);
            }
            return true;

        } catch (Exception e) {
            logger.error("批量删除站点失败，错误：{}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean batchUpdateSiteStatus(List<Long> siteIds, String status, Long adminId) {
        logger.info("批量更新站点状态，站点数量：{}，新状态：{}", siteIds.size(), status);

        try {
            int updateCount = siteMapper.update(Wrappers.<Site>lambdaUpdate()
                    .set(Site::getStatus, status)
                    .in(Site::getId, siteIds));
            logger.info("批量更新站点状态成功，影响行数：{}", updateCount);
            return updateCount > 0;

        } catch (Exception e) {
            logger.error("批量更新站点状态失败，错误：{}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean enableSite(Long siteId, Long adminId) {
        logger.info("启用站点，站点ID：{}", siteId);
        return updateSiteStatus(siteId, AdminConstants.SiteStatus.ACTIVE, adminId);
    }

    @Override
    public Boolean disableSite(Long siteId, Long adminId) {
        logger.info("停用站点，站点ID：{}", siteId);
        return updateSiteStatus(siteId, AdminConstants.SiteStatus.INACTIVE, adminId);
    }

    @Override
    public Boolean setMaintenanceStatus(Long siteId, Long adminId) {
        logger.info("设置站点维护状态，站点ID：{}", siteId);
        return updateSiteStatus(siteId, AdminConstants.SiteStatus.MAINTENANCE, adminId);
    }

    @Override
    public List<SiteListVo> searchSitesByKeyword(String keyword) {
        logger.info("根据关键字搜索站点，关键字：{}", keyword);

        List<Site> sites = siteMapper.selectList(Wrappers.<Site>lambdaQuery()
                .like(Site::getSiteName, keyword));
        return sites.stream()
                .map(this::convertToSiteListVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<SiteListVo> getSitesByCoordinateRange(Double minLongitude, Double maxLongitude,
                                                      Double minLatitude, Double maxLatitude) {
        logger.info("根据坐标范围查询站点");

        List<Site> sites = siteMapper.selectList(Wrappers.<Site>lambdaQuery()
            .ge(Site::getLongitude, minLongitude)
            .le(Site::getLongitude, maxLongitude)
            .ge(Site::getLatitude, minLatitude)
            .le(Site::getLatitude, maxLatitude)
        );
        return sites.stream()
                .map(this::convertToSiteListVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCities() {
        logger.info("获取所有城市列表");
        return siteMapper.selectObjs(Wrappers.<Site>query().select("DISTINCT city"));
    }

    @Override
    public List<String> getAllLineNames() {
        logger.info("获取所有线路名称列表");
        return siteMapper.selectObjs(Wrappers.<Site>query().select("DISTINCT line_name"));
    }

    @Override
    public SiteStatisticsVo getSiteStatistics() {
        logger.info("获取站点统计信息");
        
        try {
            // 查询站点总数
            Long totalSites = siteMapper.selectCount(null);
            
            // 按状态统计站点数量
            Long activeSites = siteMapper.selectCount(Wrappers.<Site>lambdaQuery()
                    .eq(Site::getStatus, AdminConstants.SiteStatus.ACTIVE));
            
            Long inactiveSites = siteMapper.selectCount(Wrappers.<Site>lambdaQuery()
                    .eq(Site::getStatus, AdminConstants.SiteStatus.INACTIVE));
            
            Long maintenanceSites = siteMapper.selectCount(Wrappers.<Site>lambdaQuery()
                    .eq(Site::getStatus, AdminConstants.SiteStatus.MAINTENANCE));
            
            // 统计城市数量（去重）
            List<String> cities = getAllCities();
            Integer totalCities = cities != null ? cities.size() : 0;
            
            // 构建统计信息VO
            SiteStatisticsVo statistics = new SiteStatisticsVo();
            statistics.setTotalSites(totalSites.intValue());
            statistics.setActiveSites(activeSites.intValue());
            statistics.setInactiveSites(inactiveSites.intValue());
            statistics.setMaintenanceSites(maintenanceSites.intValue());
            statistics.setTotalCities(totalCities);

            logger.info("获取站点统计信息成功 - 总数: {}, 正常: {}, 停用: {}, 维护: {}, 城市数: {}", 
                       totalSites, activeSites, inactiveSites, maintenanceSites, totalCities);
            
            return statistics;
            
        } catch (Exception e) {
            logger.error("获取站点统计信息失败，错误：{}", e.getMessage(), e);
            return null;
        }
    }


    @Override
    public Boolean checkSiteCodeExists(String siteCode, Long excludeId) {
        int count = Math.toIntExact(siteMapper.selectCount(Wrappers.<Site>lambdaQuery()
                        .eq(Site::getSiteCode, siteCode)
                        .ne(Site::getId, excludeId)
        ));
        return count > 0;
    }

    @Override
    public Boolean checkSiteNameExists(String siteName, Long excludeId) {
        int count = Math.toIntExact(siteMapper.selectCount(Wrappers.<Site>lambdaQuery()
                        .eq(Site::getSiteName, siteName)
                        .ne(Site::getId, excludeId)
        ));
        return count > 0;
    }

    private Boolean updateSiteStatus(Long siteId, String status, Long adminId) {
        try {
            Site site = siteMapper.selectById(siteId);
            if (site == null) {
                return false;
            }

            site.setStatus(status);
            site.setUpdatedTime(LocalDateTime.now());

            int updateCount = siteMapper.updateById(site);
            return updateCount > 0;

        } catch (Exception e) {
            logger.error("更新站点状态失败，站点ID：{}，错误：{}", siteId, e.getMessage(), e);
            return false;
        }
    }

    private SiteListVo convertToSiteListVo(Site site) {
        return SiteListVo.builder()
                .siteId(site.getId())
                .siteName(site.getSiteName())
                .siteCode(site.getSiteCode())
                .siteAddress(site.getAddress())
                .contactPerson(null) // Not available in Site entity
                .contactPhone(null) // Not available in Site entity
                .status(site.getStatus())
                .siteType(null) // Not available in Site entity
                .type(site.getType())
                .city(site.getCity())
                .lineName(site.getLineName())
                .longitude(site.getLongitude())
                .latitude(site.getLatitude())
                .createdTime(site.getCreatedTime() != null ?
                    LocalDateTime.now() : null)
                .updatedTime(site.getUpdatedTime() != null ? 
                    LocalDateTime.now() : null)
                .build();
    }

    private SiteDetailVo convertToSiteDetailVo(Site site) {
        return SiteDetailVo.builder()
                .siteId(site.getId())
                .siteName(site.getSiteName())
                .siteCode(site.getSiteCode())
                .siteAddress(site.getAddress())
                .status(site.getStatus())
                .type(site.getType())
                .city(site.getCity())
                .lineName(site.getLineName())
                .longitude(site.getLongitude())
                .latitude(site.getLatitude())
                .createdTime(site.getCreatedTime() != null ?
                    LocalDateTime.now() : null)
                .updatedTime(site.getUpdatedTime() != null ? 
                    LocalDateTime.now() : null)
                .devices(new ArrayList<>())
                .passengerFlowStats(null) // Will be set in getSiteDetail method
                .build();
    }

    /**
     * 转换设备列表为设备简单视图对象列表
     */
    private List<SiteDetailVo.DeviceSimpleVo> convertToDeviceSimpleVos(List<TurnstileDevice> devices) {
        if (devices == null || devices.isEmpty()) {
            return new ArrayList<>();
        }

        return devices.stream()
                .map(this::convertToDeviceSimpleVo)
                .collect(Collectors.toList());
    }

    /**
     * 转换单个设备为设备简单视图对象
     */
    private SiteDetailVo.DeviceSimpleVo convertToDeviceSimpleVo(TurnstileDevice device) {
        return SiteDetailVo.DeviceSimpleVo.builder()
                .deviceId(device.getId())
                .deviceCode(device.getDeviceCode())
                .deviceName(device.getDeviceName())
                .status(device.getStatus())
                .build();
    }

    /**
     * 计算站点客流量统计信息
     */
    private SiteDetailVo.PassengerFlowStats calculatePassengerFlowStats(Long siteId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();

            // 查询今日进站人数
            Long todayEntryCount = transitRecordMapper.selectCount(
                Wrappers.<TransitRecord>lambdaQuery()
                    .eq(TransitRecord::getEntrySiteId, siteId)
                    .ge(TransitRecord::getEntryTime, startOfToday)
                    .lt(TransitRecord::getEntryTime, now)
            );

            // 查询今日出站人数
            Long todayExitCount = transitRecordMapper.selectCount(
                Wrappers.<TransitRecord>lambdaQuery()
                    .eq(TransitRecord::getExitSiteId, siteId)
                    .ge(TransitRecord::getExitTime, startOfToday)
                    .lt(TransitRecord::getExitTime, now)
            );

            // 计算当前月份日均客流量
            Long monthlyTotalFlow = transitRecordMapper.selectCount(
                Wrappers.<TransitRecord>lambdaQuery()
                    .and(wrapper -> wrapper
                        .eq(TransitRecord::getEntrySiteId, siteId)
                        .or()
                        .eq(TransitRecord::getExitSiteId, siteId)
                    )
                    .ge(TransitRecord::getCreatedTime, startOfMonth)
                    .lt(TransitRecord::getCreatedTime, now)
            );

            // 计算当前月份已经过的天数
            int daysInMonth = now.getDayOfMonth();
            Double averageDailyFlow = daysInMonth > 0 ? monthlyTotalFlow.doubleValue() / daysInMonth : 0.0;

            // 构建统计结果
            return SiteDetailVo.PassengerFlowStats.builder()
                    .todayEntryCount(todayEntryCount)
                    .todayExitCount(todayExitCount)
                    .todayTotalFlow(todayEntryCount + todayExitCount)
                    .averageDailyFlow(averageDailyFlow)
                    .build();

        } catch (Exception e) {
            logger.error("计算站点客流量统计失败，站点ID：{}，错误：{}", siteId, e.getMessage(), e);
            // 返回空统计数据
            return SiteDetailVo.PassengerFlowStats.builder()
                    .todayEntryCount(0L)
                    .todayExitCount(0L)
                    .todayTotalFlow(0L)
                    .averageDailyFlow(0.0)
                    .build();
        }
    }
}

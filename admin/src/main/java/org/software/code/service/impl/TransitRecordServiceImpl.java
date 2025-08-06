package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.entity.Site;
import org.software.code.entity.TransitRecord;
import org.software.code.entity.TurnstileDevice;
import org.software.code.mapper.SiteMapper;
import org.software.code.mapper.TransitRecordMapper;
import org.software.code.mapper.TurnstileDeviceMapper;
import org.software.code.service.TransitRecordService;
import org.software.code.vo.TransitRecordVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 出行记录服务实现类
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Service
public class TransitRecordServiceImpl extends ServiceImpl<TransitRecordMapper, TransitRecord> implements TransitRecordService {
    private static final Logger logger = LoggerFactory.getLogger(TransitRecordServiceImpl.class);

    @Resource
    private TransitRecordMapper transitRecordMapper;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private TurnstileDeviceMapper turnstileDeviceMapper;

    @Override
    public Page<TransitRecordVo> getUserTransitRecords(Long userId, Integer pageNum, Integer pageSize) {
        logger.info("获取用户出行记录，用户ID：{}，页码：{}，页大小：{}", userId, pageNum, pageSize);

        Page<TransitRecord> transitRecordPage = new Page<>(pageNum, pageSize);
        Page<TransitRecord> resultPage = transitRecordMapper.selectPage(
            transitRecordPage,
            Wrappers.<TransitRecord>lambdaQuery()
                .eq(TransitRecord::getUserId, userId)
                .orderByDesc(TransitRecord::getCreatedTime)
        );

        Page<TransitRecordVo> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setPages(resultPage.getPages());
        
        List<TransitRecordVo> transitRecordVos = resultPage.getRecords().stream()
                .map(this::convertToTransitRecordVo)
                .collect(Collectors.toList());
        
        voPage.setRecords(transitRecordVos);
        return voPage;
    }

    @Override
    public Page<TransitRecordVo> getAllTransitRecords(Integer pageNum, Integer pageSize) {
        logger.info("获取所有出行记录，页码：{}，页大小：{}", pageNum, pageSize);

        Page<TransitRecord> transitRecordPage = new Page<>(pageNum, pageSize);
        Page<TransitRecord> resultPage = transitRecordMapper.selectPage(
            transitRecordPage,
            Wrappers.<TransitRecord>lambdaQuery()
                .orderByDesc(TransitRecord::getCreatedTime)
        );

        Page<TransitRecordVo> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setPages(resultPage.getPages());
        
        List<TransitRecordVo> transitRecordVos = resultPage.getRecords().stream()
                .map(this::convertToTransitRecordVo)
                .collect(Collectors.toList());
        
        voPage.setRecords(transitRecordVos);
        return voPage;
    }

    private TransitRecordVo convertToTransitRecordVo(TransitRecord transitRecord) {
        String entrySiteName = null;
        String exitSiteName = null;
        String city = null;
        String entryDeviceName = null;
        String exitDeviceName = null;
        
        // 查询进站站点信息
        if (transitRecord.getEntrySiteId() != null) {
            Site entrySite = siteMapper.selectById(transitRecord.getEntrySiteId());
            if (entrySite != null) {
                entrySiteName = entrySite.getSiteName();
                city = entrySite.getCity(); // 从进站站点获取城市信息
            }
        }
        
        // 查询出站站点信息
        if (transitRecord.getExitSiteId() != null) {
            Site exitSite = siteMapper.selectById(transitRecord.getExitSiteId());
            if (exitSite != null) {
                exitSiteName = exitSite.getSiteName();
                // 如果进站站点没有城市信息，从出站站点获取
                if (city == null) {
                    city = exitSite.getCity();
                }
            }
        }
        
        // 查询进站设备信息
        if (transitRecord.getEntryDeviceId() != null) {
            TurnstileDevice entryDevice = turnstileDeviceMapper.selectById(transitRecord.getEntryDeviceId());
            entryDeviceName = entryDevice != null ? entryDevice.getDeviceName() : null;
        }
        
        // 查询出站设备信息
        if (transitRecord.getExitDeviceId() != null) {
            TurnstileDevice exitDevice = turnstileDeviceMapper.selectById(transitRecord.getExitDeviceId());
            exitDeviceName = exitDevice != null ? exitDevice.getDeviceName() : null;
        }
        
        Long durationMinutes = null;
        if (transitRecord.getEntryTime() != null && transitRecord.getExitTime() != null) {
            long diffInSeconds = java.time.Duration.between(transitRecord.getEntryTime(), transitRecord.getExitTime()).getSeconds();
            durationMinutes = diffInSeconds / 60;
        }
        
        String statusName = getStatusName(transitRecord.getStatus());
        
        return TransitRecordVo.builder()
                .userId(transitRecord.getUserId())
                .mode(transitRecord.getMode())
                .city(city)
                .entrySiteName(entrySiteName)
                .exitSiteName(exitSiteName)
                .entryDeviceName(entryDeviceName)
                .exitDeviceName(exitDeviceName)
                .entryTime(transitRecord.getEntryTime())
                .exitTime(transitRecord.getExitTime())
                .amount(transitRecord.getAmount())
                .discountAmount(transitRecord.getDiscountAmount())
                .actualAmount(transitRecord.getActualAmount())
                .status(transitRecord.getStatus())
                .statusName(statusName)
                .reason(transitRecord.getReason())
                .transactionId(transitRecord.getTransactionId())
                .durationMinutes(durationMinutes)
                .createdTime(transitRecord.getCreatedTime())
                .updatedTime(transitRecord.getUpdatedTime())
                .build();
    }
    
    private String getStatusName(Integer status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case 0: return "正常";
            case 1: return "支付异常";
            case 2: return "出行异常";
            default: return "未知状态";
        }
    }
}
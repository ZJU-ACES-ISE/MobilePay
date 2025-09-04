package org.software.code.controller;

import org.software.code.entity.Site;
import org.software.code.entity.TurnstileDevice;
import org.software.code.mapper.SiteMapper;
import org.software.code.mapper.TurnstileDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.List;

/**
 * 管理服务内部API控制器
 * 用于处理来自其他服务的Feign调用
 */
@RestController
@RequestMapping("/admin")
public class InternalAdminController {

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private TurnstileDeviceMapper turnstileDeviceMapper;

    /**
     * 根据站点ID获取站点信息
     */
    @GetMapping("/sites/{siteId}")
    public Site getSiteById(@PathVariable("siteId") Long siteId) {
        return siteMapper.selectById(siteId);
    }

    /**
     * 根据站点名称获取站点信息
     */
    @GetMapping("/sites/by-name/{siteName}")
    public Site getSiteByName(@PathVariable("siteName") String siteName) {
        LambdaQueryWrapper<Site> queryWrapper = Wrappers.<Site>lambdaQuery()
                .eq(Site::getSiteName, siteName);
        return siteMapper.selectOne(queryWrapper);
    }

    /**
     * 根据城市获取站点列表
     */
    @GetMapping("/sites/by-city")
    public List<Site> getSitesByCity(@RequestParam("city") String city,
                                   @RequestParam(value = "type", required = false) String type) {
        LambdaQueryWrapper<Site> queryWrapper = Wrappers.<Site>lambdaQuery()
                .eq(Site::getCity, city)
                .eq(Site::getStatus, "ACTIVE");

        if (type != null && !type.isEmpty()) {
            queryWrapper.eq(Site::getType, type);
        }

        return siteMapper.selectList(queryWrapper);
    }

    /**
     * 获取闸机设备信息
     */
    @GetMapping("/devices/{deviceId}")
    public TurnstileDevice getTurnstileDevice(@PathVariable("deviceId") Long deviceId) {
        return turnstileDeviceMapper.selectById(deviceId);
    }
} 
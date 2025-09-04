package org.software.code.client;

import org.software.code.entity.Site;
import org.software.code.entity.TurnstileDevice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 管理服务Feign客户端
 * 用于appPayment模块调用admin服务
 */
@FeignClient(name = "admin", path = "/admin")
public interface AdminClient {

    /**
     * 根据站点ID获取站点信息
     */
    @GetMapping("/sites/{siteId}")
    Site getSiteById(@PathVariable("siteId") Long siteId);

    /**
     * 根据站点名称获取站点信息
     */
    @GetMapping("/sites/by-name/{siteName}")
    Site getSiteByName(@PathVariable("siteName") String siteName);

    /**
     * 根据城市获取站点列表
     */
    @GetMapping("/sites/by-city")
    List<Site> getSitesByCity(@RequestParam("city") String city,
                             @RequestParam(value = "type", required = false) String type);

    /**
     * 获取闸机设备信息
     */
    @GetMapping("/devices/{deviceId}")
    TurnstileDevice getTurnstileDevice(@PathVariable("deviceId") Long deviceId);
} 
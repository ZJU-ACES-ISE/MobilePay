package org.software.code.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DeviceUpdateDto 是设备更新请求数据传输对象，用于封装更新设备的请求信息
 *
 */
@Data
public class DeviceUpdateDto {

    /**
     * 设备名称
     */
    @Size(max = 100, message = "设备名称长度不能超过100字符")
    private String deviceName;

    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * 设备类型：ENTRY, EXIT, BOTH
     */
    @Pattern(regexp = "^(ENTRY|EXIT|BOTH)$", message = "设备类型只能是ENTRY、EXIT或BOTH")
    private String deviceType;

    /**
     * 设备状态：ONLINE, OFFLINE, MAINTENANCE, FAULT
     */
    @Pattern(regexp = "^(ONLINE|OFFLINE|MAINTENANCE|FAULT)$", message = "设备状态只能是ONLINE、OFFLINE、MAINTENANCE或FAULT")
    private String status;

    /**
     * 设备描述
     */
    @Size(max = 500, message = "设备描述长度不能超过500字符")
    private String description;

    /**
     * 固件版本
     */
    @Size(max = 50, message = "固件版本长度不能超过50字符")
    private String firmwareVersion;

    /**
     * IP地址
     */
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", 
             message = "IP地址格式不正确")
    private String ipAddress;

    /**
     * MAC地址
     */
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", 
             message = "MAC地址格式不正确")
    private String macAddress;

    /**
     * 设备位置描述
     */
    @Size(max = 200, message = "设备位置描述长度不能超过200字符")
    private String location;

    /**
     * 交通类型：SUBWAY, BUS
     */
    @Pattern(regexp = "^(SUBWAY|BUS)$", message = "交通类型只能是SUBWAY或BUS")
    private String type;
}
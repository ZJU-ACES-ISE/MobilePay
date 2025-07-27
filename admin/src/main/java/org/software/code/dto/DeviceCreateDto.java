package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DeviceCreateDto 是设备创建请求数据传输对象，用于封装创建设备的请求信息
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCreateDto {

    /**
     * 设备编码（唯一）
     */
    @NotBlank(message = "设备编码不能为空")
    @Pattern(regexp = "^[A-Z0-9]{8,20}$", message = "设备编码必须是8-20位字母或数字组合")
    @JsonProperty("device_code")
    private String deviceCode;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    @Size(max = 100, message = "设备名称长度不能超过100字符")
    @JsonProperty("device_name")
    private String deviceName;

    /**
     * 站点ID
     */
    @NotNull(message = "站点ID不能为空")
    @JsonProperty("site_id")
    private Long siteId;

    /**
     * 设备类型：ENTRY, EXIT, BOTH
     */
    @NotBlank(message = "设备类型不能为空")
    @Pattern(regexp = "^(ENTRY|EXIT|BOTH)$", message = "设备类型只能是ENTRY、EXIT或BOTH")
    @JsonProperty("device_type")
    private String deviceType;

    /**
     * 设备描述
     */
    @Size(max = 500, message = "设备描述长度不能超过500字符")
    @JsonProperty("description")
    private String description;

    /**
     * 固件版本
     */
    @Size(max = 50, message = "固件版本长度不能超过50字符")
    @JsonProperty("firmware_version")
    private String firmwareVersion;

    /**
     * IP地址
     */
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", 
             message = "IP地址格式不正确")
    @JsonProperty("ip_address")
    private String ipAddress;

    /**
     * MAC地址
     */
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", 
             message = "MAC地址格式不正确")
    @JsonProperty("mac_address")
    private String macAddress;

    /**
     * 设备位置描述
     */
    @Size(max = 200, message = "设备位置描述长度不能超过200字符")
    @JsonProperty("location")
    private String location;
}

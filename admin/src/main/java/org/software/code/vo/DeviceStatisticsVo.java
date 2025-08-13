package org.software.code.vo;

import lombok.Data;

/**
 * 设备统计数据VO
 */
@Data
public class DeviceStatisticsVo {

    private Long totalDevices;

    private Long activeDevices;

    private Long inactiveDevices;

    private Long offlineDevices;

    private Long maintenanceDevices;

}
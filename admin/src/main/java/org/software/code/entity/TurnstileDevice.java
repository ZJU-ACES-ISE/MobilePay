package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("turnstile_device")
public class TurnstileDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("device_code")
    private String deviceCode;

    @TableField("site_id")
    private Long siteId;

    @TableField("device_type")
    private String deviceType;

    @TableField("device_name")
    private String deviceName;

    @TableField("status")
    private String status;

    @TableField("last_heartbeat")
    private Date lastHeartbeat;

    @TableField("firmware_version")
    private String firmwareVersion;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;


}

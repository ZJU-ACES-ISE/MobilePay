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
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_audit_record")
public class UserAuditRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("admin_id")
    private Long adminId;

    @TableField("audit_type")
    private String auditType;

    @TableField("audit_result")
    private String auditResult;

    @TableField("audit_reason")
    private String auditReason;

    @TableField("old_data")
    private String oldData;

    @TableField("new_data")
    private String newData;

    @TableField("created_time")
    private Date createdTime;


}

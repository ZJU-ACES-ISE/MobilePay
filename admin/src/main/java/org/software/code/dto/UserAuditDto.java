package org.software.code.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * UserAuditDto 是用户审核请求数据传输对象，用于封装用户审核的请求信息
 *
 */
@Data
public class UserAuditDto {

    /**
     * 审核结果：APPROVED, REJECTED
     */
    @NotBlank(message = "审核结果不能为空")
    @Pattern(regexp = "^(APPROVED|REJECTED)$", message = "审核结果只能是APPROVED或REJECTED")
    private String auditResult;

    /**
     * 审核原因
     */
    @Size(max = 500, message = "审核原因长度不能超过500字符")
    private String auditReason;

    /**
     * 审核类型：REGISTER, PROFILE_UPDATE
     */
    @NotBlank(message = "审核类型不能为空")
    @Pattern(regexp = "^(REGISTER|PROFILE_UPDATE)$", message = "审核类型只能是REGISTER或PROFILE_UPDATE")
    private String auditType = "REGISTER";
}
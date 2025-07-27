package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * AdminLoginDto 是管理员登录请求数据传输对象，用于封装管理员登录的请求信息
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Data
public class AdminLoginDto {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    @JsonProperty("username")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    @JsonProperty("password")
    private String password;

    /**
     * 记住我（可选）
     */
    @JsonProperty("remember_me")
    private Boolean rememberMe = false;

}

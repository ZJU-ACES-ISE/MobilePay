package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * RefreshTokenDto 是刷新Token请求参数封装类
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {

    /**
     * 刷新Token
     */
    @JsonProperty("refresh_token")
    @NotBlank(message = "刷新Token不能为空")
    private String refreshToken;
}
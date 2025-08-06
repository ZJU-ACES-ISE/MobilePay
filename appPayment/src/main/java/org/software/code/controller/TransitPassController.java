package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.software.code.common.result.Result;
import org.software.code.dto.TransitPassRequestDto;
import org.software.code.service.TransitPassService;
import org.software.code.vo.CityVo;
import org.software.code.vo.TransitPassVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 通行码控制器
 */
@RestController
@RequestMapping("/transit/pass")
@Tag(name = "通行码管理", description = "通行码相关接口")
public class TransitPassController {

    @Autowired
    private TransitPassService transitPassService;
    
    @Operation(summary = "获取用户的所有通行码", description = "获取当前登录用户的所有通行码")
    @GetMapping("/list")
    public Result<List<TransitPassVo>> getUserTransitPasses(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization) {
        return transitPassService.getUserTransitPasses(authorization);
    }
    
    @Operation(summary = "根据城市ID获取用户的通行码", description = "获取当前登录用户指定城市的通行码")
    @GetMapping("/city/{cityId}")
    public Result<TransitPassVo> getUserTransitPassByCity(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "城市ID", required = true)
            @PathVariable("cityId") String cityId) {
        return transitPassService.getUserTransitPassByCity(authorization, cityId);
    }
    
    @Operation(summary = "生成或更新通行码", description = "生成或更新当前登录用户指定城市的通行码")
    @PostMapping("/generate")
    public Result<TransitPassVo> generateOrUpdateTransitPass(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "请求参数", required = true)
            @RequestBody @Valid TransitPassRequestDto requestDto) {
        return transitPassService.generateOrUpdateTransitPass(authorization, requestDto);
    }
    
    @Operation(summary = "获取支持的城市列表", description = "获取系统支持的城市列表")
    @GetMapping("/cities")
    public Result<List<CityVo>> getSupportedCities() {
        return transitPassService.getSupportedCities();
    }
} 
package org.software.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.software.code.common.result.Result;
import org.software.code.dto.TransitEntryRequestDto;
import org.software.code.dto.TransitExitRequestDto;
import org.software.code.dto.TransitRepayRequestDto;
import org.software.code.service.TransitService;
import org.software.code.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 出行控制器
 */
@RestController
@RequestMapping("/transit")
@Tag(name = "出行管理", description = "出行相关接口")
public class TransitController {

    @Autowired
    private TransitService transitService;
    
    @Operation(summary = "入站", description = "用户进入站点时调用")
    @PostMapping("/entry")
    public Result<TransitEntryResponseVo> entryStation(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "入站请求参数", required = true)
            @RequestBody @Valid TransitEntryRequestDto requestDto) {
        return transitService.entryStation(authorization, requestDto);
    }
    
    @Operation(summary = "出站", description = "用户离开站点时调用，记录出站信息并计算费用、时长")
    @PostMapping("/exit")
    public Result<TransitExitResponseVo> exitStation(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "出站请求参数", required = true)
            @RequestBody @Valid TransitExitRequestDto requestDto) {
        return transitService.exitStation(authorization, requestDto);
    }
    
    @Operation(summary = "获取用户的出行记录", description = "查询用户的历史出行记录")
    @GetMapping("/records")
    public Result<List<TransitRecordVo>> getUserTransitRecords(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "限制记录数，默认10条")
            @RequestParam(value = "limit", required = false) Integer limit) {
        return transitService.getUserTransitRecords(authorization, limit);
    }

    
    @Operation(summary = "获取出站详情", description = "查询指定出行ID的详细信息")
    @GetMapping("/detail/{transitId}")
    public Result<TransitDetailVo> getTransitDetail(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "出行ID", required = true)
            @PathVariable("transitId") String transitId) {
        return transitService.getTransitDetail(authorization, transitId);
    }
    
    @Operation(summary = "支付异常补缴", description = "对支付异常的出行记录进行补缴")
    @PostMapping("/repay")
    public Result<TransitRepayResponseVo> repayTransit(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "补缴请求参数", required = true)
            @RequestBody @Valid TransitRepayRequestDto requestDto) {
        return transitService.repayTransit(authorization, requestDto);
    }
    
    @Operation(summary = "获取城市站点列表", description = "获取指定城市的所有站点")
    @GetMapping("/sites")
    public Result<List<SiteVo>> getCityStations(
            @Parameter(description = "Bearer 类型 Token 认证", required = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "城市名称", required = true)
            @RequestParam("city") String city,
            @Parameter(description = "站点类型，可选值：SUBWAY-地铁站，BUS-公交站，不传则查询所有类型")
            @RequestParam(value = "type", required = false) String type) {
        return transitService.getCityStations(authorization, city, type);
    }
} 
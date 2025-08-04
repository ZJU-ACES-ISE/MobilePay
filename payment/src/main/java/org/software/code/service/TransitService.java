package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.TransitEntryRequestDto;
import org.software.code.dto.TransitExitRequestDto;
import org.software.code.dto.TransitRepayRequestDto;
import org.software.code.vo.*;

import java.util.List;

/**
 * 出行服务接口
 */
public interface TransitService {
    
    /**
     * 入站
     * @param authorization 认证token
     * @param requestDto 入站请求参数
     * @return 入站记录
     */
    Result<TransitEntryResponseVo> entryStation(String authorization, TransitEntryRequestDto requestDto);
    
    /**
     * 出站
     * @param authorization 认证token
     * @param requestDto 出站请求参数
     * @return 出站结果
     */
    Result<TransitExitResponseVo> exitStation(String authorization, TransitExitRequestDto requestDto);

    /**
     * 获取用户的出行记录
     * @param authorization 认证token
     * @param limit 限制记录数，默认10条
     * @return 出行记录列表
     */
    Result<List<TransitRecordVo>> getUserTransitRecords(String authorization, Integer limit);
    
    /**
     * 获取出站详情
     * @param authorization 认证token
     * @param transitId 出行ID
     * @return 出站详情
     */
    Result<TransitDetailVo> getTransitDetail(String authorization, String transitId);
    
    /**
     * 支付异常补缴
     * @param authorization 认证token
     * @param requestDto 补缴请求参数
     * @return 补缴结果
     */
    Result<TransitRepayResponseVo> repayTransit(String authorization, TransitRepayRequestDto requestDto);
    
    /**
     * 获取指定城市的站点列表
     * @param authorization 认证token
     * @param city 城市名称
     * @param type 站点类型（可选，SUBWAY-地铁站，BUS-公交站）
     * @return 站点列表
     */
    Result<List<SiteVo>> getCityStations(String authorization, String city, String type);
} 
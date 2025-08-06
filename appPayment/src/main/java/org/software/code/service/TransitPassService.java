package org.software.code.service;

import org.software.code.common.result.Result;
import org.software.code.dto.TransitPassRequestDto;
import org.software.code.vo.CityVo;
import org.software.code.vo.TransitPassVo;

import java.util.List;

/**
 * 通行码服务接口
 */
public interface TransitPassService {
    
    /**
     * 获取用户的所有通行码
     * @param authorization 认证token
     * @return 通行码列表
     */
    Result<List<TransitPassVo>> getUserTransitPasses(String authorization);
    
    /**
     * 根据城市ID获取用户的通行码
     * @param authorization 认证token
     * @param cityId 城市ID
     * @return 通行码
     */
    Result<TransitPassVo> getUserTransitPassByCity(String authorization, String cityId);
    
    /**
     * 生成或更新通行码
     * @param authorization 认证token
     * @param requestDto 请求参数
     * @return 通行码
     */
    Result<TransitPassVo> generateOrUpdateTransitPass(String authorization, TransitPassRequestDto requestDto);
    
    /**
     * 获取支持的城市列表
     * @return 城市列表
     */
    Result<List<CityVo>> getSupportedCities();
} 
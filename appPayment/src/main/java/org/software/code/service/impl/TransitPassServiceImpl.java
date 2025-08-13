package org.software.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.software.code.common.result.Result;
import org.software.code.common.result.ResultEnum;
import org.software.code.common.util.JwtUtil;
import org.software.code.common.util.OSSUtil;
import org.software.code.common.util.QRCodeUtil;
import org.software.code.dto.TransitPassRequestDto;
import org.software.code.entity.TransitPass;
import org.software.code.mapper.TransitPassMapper;
import org.software.code.service.TransitPassService;
import org.software.code.vo.CityVo;
import org.software.code.vo.TransitPassVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 通行码服务实现类
 */
@Service
public class TransitPassServiceImpl implements TransitPassService {


    
    @Autowired
    private TransitPassMapper transitPassMapper;
    
    @Autowired
    private QRCodeUtil qrCodeUtil;
    
    @Autowired
    private OSSUtil ossUtil;
    
    @Override
    public Result<List<TransitPassVo>> getUserTransitPasses(String authorization) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询用户的所有通行码
            LambdaQueryWrapper<TransitPass> queryWrapper = Wrappers.<TransitPass>lambdaQuery()
                    .eq(TransitPass::getUserId, userId)
                    .eq(TransitPass::getStatus, "ACTIVE");
            List<TransitPass> transitPasses = transitPassMapper.selectList(queryWrapper);
            
            // 如果没有通行码，则创建默认的通行码（北京、上海、广州、深圳）
            if (transitPasses == null || transitPasses.isEmpty()) {
                transitPasses = createDefaultTransitPasses(userId);
            }
            
            // 转换为VO
            List<TransitPassVo> transitPassVos = transitPasses.stream()
                    .map(this::convertToVo)
                    .collect(Collectors.toList());
            
            return Result.success("查询成功", transitPassVos);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    @Override
    public Result<TransitPassVo> getUserTransitPassByCity(String authorization, String cityId) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询用户指定城市的通行码
            LambdaQueryWrapper<TransitPass> queryWrapper = Wrappers.<TransitPass>lambdaQuery()
                    .eq(TransitPass::getUserId, userId)
                    .eq(TransitPass::getCityId, cityId)
                    .eq(TransitPass::getStatus, "ACTIVE");
            TransitPass transitPass = transitPassMapper.selectOne(queryWrapper);
            
            // 如果没有通行码，则返回错误
            if (transitPass == null) {
                return Result.instance(ResultEnum.FAILED.getCode(), "未找到该城市的通行码", null);
            }
            
            // 转换为VO
            TransitPassVo transitPassVo = convertToVo(transitPass);
            
            return Result.success("查询成功", transitPassVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    @Override
    public Result<TransitPassVo> generateOrUpdateTransitPass(String authorization, TransitPassRequestDto requestDto) {
        try {
            // 从token中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Long userId = JwtUtil.extractID(token);
            
            // 查询用户指定城市的通行码
            LambdaQueryWrapper<TransitPass> queryWrapper = Wrappers.<TransitPass>lambdaQuery()
                    .eq(TransitPass::getUserId, userId)
                    .eq(TransitPass::getCityId, requestDto.getCityId());
            TransitPass transitPass = transitPassMapper.selectOne(queryWrapper);
            
            // 如果没有通行码，则创建新的通行码
            if (transitPass == null) {
                transitPass = new TransitPass();
                transitPass.setUserId(userId);
                transitPass.setCityId(requestDto.getCityId());
                transitPass.setCityName(requestDto.getCityName());
                transitPass.setStatus("ACTIVE");
                
                // 生成通行码链接
                String codeUrl = generateTransitPassUrl(userId, requestDto.getCityId(), requestDto.getCityName());
                transitPass.setCodeUrl(codeUrl);
                
                transitPassMapper.insert(transitPass);
            } else {
                // 更新通行码
                transitPass.setCityName(requestDto.getCityName());
                
                // 重新生成通行码链接
                String codeUrl = generateTransitPassUrl(userId, requestDto.getCityId(), requestDto.getCityName());
                transitPass.setCodeUrl(codeUrl);
                
                transitPass.setUpdatedTime(LocalDateTime.now());
                transitPassMapper.updateById(transitPass);
            }
            
            // 转换为VO
            TransitPassVo transitPassVo = convertToVo(transitPass);
            
            return Result.success("生成成功", transitPassVo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    @Override
    public Result<List<CityVo>> getSupportedCities() {
        try {
            // 创建默认的城市列表
            List<CityVo> cities = new ArrayList<>();
            
            // 北京
            cities.add(CityVo.builder()
                    .id(1L)
                    .cityId("1100")
                    .cityName("北京")
                    .build());
            
            // 上海
            cities.add(CityVo.builder()
                    .id(2L)
                    .cityId("3100")
                    .cityName("上海")
                    .build());
            
            // 广州
            cities.add(CityVo.builder()
                    .id(3L)
                    .cityId("4401")
                    .cityName("广州")
                    .build());
            
            // 深圳
            cities.add(CityVo.builder()
                    .id(4L)
                    .cityId("4403")
                    .cityName("深圳")
                    .build());
            
            return Result.success("查询成功", cities);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.instance(ResultEnum.FAILED.getCode(), "服务器内部错误", null);
        }
    }
    
    /**
     * 创建默认的通行码（北京、上海、广州、深圳）
     * @param userId 用户ID
     * @return 通行码列表
     */
    private List<TransitPass> createDefaultTransitPasses(Long userId) {
        List<TransitPass> transitPasses = new ArrayList<>();
        
        // 北京
        TransitPass beijing = new TransitPass();
        beijing.setUserId(userId);
        beijing.setCityId("1100");
        beijing.setCityName("北京");
        beijing.setCodeUrl(generateTransitPassUrl(userId, "1100", "北京"));
        beijing.setStatus("ACTIVE");
        transitPassMapper.insert(beijing);
        transitPasses.add(beijing);
        
        // 上海
        TransitPass shanghai = new TransitPass();
        shanghai.setUserId(userId);
        shanghai.setCityId("3100");
        shanghai.setCityName("上海");
        shanghai.setCodeUrl(generateTransitPassUrl(userId, "3100", "上海"));
        shanghai.setStatus("ACTIVE");
        transitPassMapper.insert(shanghai);
        transitPasses.add(shanghai);
        
        // 广州
        TransitPass guangzhou = new TransitPass();
        guangzhou.setUserId(userId);
        guangzhou.setCityId("4401");
        guangzhou.setCityName("广州");
        guangzhou.setCodeUrl(generateTransitPassUrl(userId, "4401", "广州"));
        guangzhou.setStatus("ACTIVE");
        transitPassMapper.insert(guangzhou);
        transitPasses.add(guangzhou);
        
        // 深圳
        TransitPass shenzhen = new TransitPass();
        shenzhen.setUserId(userId);
        shenzhen.setCityId("4403");
        shenzhen.setCityName("深圳");
        shenzhen.setCodeUrl(generateTransitPassUrl(userId, "4403", "深圳"));
        shenzhen.setStatus("ACTIVE");
        transitPassMapper.insert(shenzhen);
        transitPasses.add(shenzhen);
        
        return transitPasses;
    }
    
    /**
     * 生成通行码链接
     * @param userId 用户ID
     * @param cityId 城市ID
     * @param cityName 城市名称
     * @return 通行码链接
     */
    private String generateTransitPassUrl(Long userId, String cityId, String cityName) {
        try {
            // 生成唯一标识
            String uuid = UUID.randomUUID().toString().replace("-", "");
            
            // 构建通行码数据
            String codeData = String.format("user:%d,city:%s,uuid:%s,time:%d", 
                    userId, cityId, uuid, System.currentTimeMillis());
            
            // 生成文件名
            String fileName = String.format("transit/pass/%s_%s_%s", userId, cityId, uuid);
            
            // 使用QRCodeUtil生成二维码并上传到OSS
            return qrCodeUtil.generateQRCodeAndUpload(codeData, fileName);
            
        } catch (Exception e) {
            e.printStackTrace();
            // 如果生成失败，返回一个默认URL
            return String.format("https://example.com/transit/pass/%s_%s_default.png", userId, cityId);
        }
    }
    
    /**
     * 将实体转换为VO
     * @param transitPass 通行码实体
     * @return 通行码VO
     */
    private TransitPassVo convertToVo(TransitPass transitPass) {
        if (transitPass == null) {
            return null;
        }
        
        TransitPassVo vo = new TransitPassVo();
        BeanUtils.copyProperties(transitPass, vo);
        return vo;
    }
} 
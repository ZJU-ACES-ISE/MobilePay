package org.software.code.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.software.code.common.annotation.AdminRole;
import org.software.code.common.except.BusinessException;
import org.software.code.common.result.Result;
import org.software.code.dto.SiteCreateDto;
import org.software.code.dto.SiteSearchDto;
import org.software.code.dto.SiteUpdateDto;
import org.software.code.service.SiteService;
import org.software.code.vo.SiteDetailVo;
import org.software.code.vo.SiteListVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SiteController 是站点管理控制器，提供地铁站点信息的全生命周期管理REST API接口。
 * 
 * <p>本控制器负责处理地铁站点的基础数据管理，包括：</p>
 * <ul>
 *   <li>站点信息的增删改查：支持分页查询、详情查看、创建、更新和删除</li>
 *   <li>站点搜索功能：支持按名称、编码、线路等多维度搜索</li>
 *   <li>基础数据统计：提供城市列表、线路名称等统计信息</li>
 *   <li>站点状态管理：支持运营状态的维护和更新</li>
 * </ul>
 * 
 * <p>权限控制说明：</p>
 * <ul>
 *   <li>ADMIN角色：可查看、编辑站点信息</li>
 *   <li>SUPER_ADMIN角色：具有所有ADMIN权限，另外可创建和删除站点</li>
 * </ul>
 * 
 * <p>数据特性：</p>
 * <ul>
 *   <li>站点编码全局唯一，不可重复</li>
 *   <li>支持地理坐标信息，用于地图定位和距离计算</li>
 *   <li>关联闸机设备管理，支持级联查询</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@RestController
@RequestMapping("/site")
@Tag(name = "站点管理", description = "站点信息的增删改查和状态管理")
@Validated
public class SiteController {

    private static final Logger logger = LoggerFactory.getLogger(SiteController.class);

    @Resource
    private SiteService siteService;

    /**
     * 分页查询站点列表接口
     * 
     * <p>此接口用于管理员分页查看系统中所有地铁站点的基本信息，支持多种过滤条件。</p>
     * 
     * <p>支持的查询条件：</p>
     * <ul>
     *   <li>城市筛选：按城市名称筛选站点</li>
     *   <li>线路筛选：按地铁线路名称筛选</li>
     *   <li>运营状态筛选：按站点运营状态筛选</li>
     *   <li>关键字搜索：支持站点名称、编码的模糊搜索</li>
     * </ul>
     * 
     * <p>返回数据包含站点的基本信息、地理位置、运营状态等，列表按站点编码排序。</p>
     *
     * @param pageNum 页码，从1开始，默认为1
     * @param pageSize 每页记录数，默认为10，最大不超过100
     * @param searchDto 查询条件，包含城市、线路、状态等筛选条件
     * @param adminId 当前管理员ID，由网关解析JWT后传递
     * @return 分页的站点列表数据
     * @see SiteSearchDto 搜索条件说明
     * @see SiteListVo 站点列表项数据说明
     */
    @GetMapping
    @Operation(summary = "分页查询站点列表", description = "根据条件分页查询站点列表")
    @AdminRole()
    public Result<?> getSitePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索条件") @ModelAttribute SiteSearchDto searchDto,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询站点列表，管理员ID：{}，页码：{}，页大小：{}，搜索条件：{}", adminId, pageNum, pageSize, searchDto);
        
        Page<SiteListVo> sitePage = siteService.getSitePage(pageNum, pageSize, searchDto);
        return Result.success(sitePage);
    }

    /**
     * 获取站点详细信息接口
     * 
     * <p>此接口用于管理员查看指定站点的详细信息，包括所有配置参数和运营数据。</p>
     * 
     * <p>返回信息包括：</p>
     * <ul>
     *   <li>基本信息：站点编码、名称、地址、所属城市</li>
     *   <li>位置信息：经纬度坐标、行政区划代码</li>
     *   <li>线路信息：所属线路名称、线路颜色等</li>
     *   <li>运营信息：运营状态、开放时间、服务时间</li>
     *   <li>设备信息：关联的闸机设备数量和状态</li>
     * </ul>
     * 
     * <p>此接口常用于站点编辑、设备管理、运营监控等场景。</p>
     *
     * @param id 站点ID，必须为正数
     * @param adminId 当前管理员ID
     * @return 站点详细信息
     * @throws BusinessException 当站点不存在时抛出
     * @see SiteDetailVo 站点详情数据说明
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取站点详情", description = "根据ID获取站点详细信息")
    @AdminRole()
    public Result<?> getSiteDetail(
            @Parameter(description = "站点ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询站点详情，管理员ID：{}，站点ID：{}", adminId, id);
        
        SiteDetailVo siteDetail = siteService.getSiteDetail(id);
        return Result.success(siteDetail);
    }

    /**
     * 创建新站点接口（仅超级管理员）
     * 
     * <p>此接口用于超级管理员创建新的地铁站点信息，需要提供完整的站点配置数据。</p>
     * 
     * <p>创建站点的必要信息：</p>
     * <ul>
     *   <li>基本信息：站点编码（全局唯一）、站点名称、所属城市</li>
     *   <li>位置信息：具体地址、经纬度坐标、行政区划代码</li>
     *   <li>线路信息：所属线路名称和线路属性</li>
     *   <li>运营信息：运营状态、开放时间等</li>
     * </ul>
     * 
     * <p>数据验证：</p>
     * <ul>
     *   <li>站点编码唯一性校验</li>
     *   <li>地理坐标格式和范围校验</li>
     *   <li>城市和线路信息的关联性校验</li>
     * </ul>
     *
     * @param createDto 站点创建信息，包含所有必要的站点数据
     * @param adminId 当前管理员ID，用于记录创建者
     * @return 操作结果响应
     * @throws BusinessException 当站点编码已存在或数据验证失败时抛出
     * @see SiteCreateDto 创建参数说明
     */
    @PostMapping
    @Operation(summary = "创建站点", description = "创建新的站点信息")
    @AdminRole("SUPER_ADMIN")
    public Result<?> createSite(
            @Parameter(description = "站点信息") @RequestBody @Valid SiteCreateDto createDto,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员创建站点，管理员ID：{}，站点编码：{}", adminId, createDto.getSiteCode());
        
        SiteDetailVo createdSite = siteService.createSite(createDto, Long.parseLong(adminId));
        return Result.success(createdSite);
    }

    /**
     * 更新站点信息接口
     * 
     * <p>此接口用于管理员更新已存在站点的信息，支持部分字段更新。</p>
     * 
     * <p>可更新的信息包括：</p>
     * <ul>
     *   <li>基本信息：站点名称、地址信息（站点编码不可更改）</li>
     *   <li>位置信息：经纬度坐标、行政区划代码</li>
     *   <li>线路信息：所属线路和线路属性</li>
     *   <li>运营信息：运营状态、开放时间等</li>
     * </ul>
     * 
     * <p>更新限制：</p>
     * <ul>
     *   <li>站点编码和创建时间不可修改</li>
     *   <li>已有闸机设备的站点修改时需谨慎</li>
     *   <li>重要参数修改需记录操作日志</li>
     * </ul>
     *
     * @param id 站点ID，必须为正数
     * @param updateDto 站点更新信息，只包含需要更新的字段
     * @param adminId 当前管理员ID，用于记录修改者
     * @return 操作结果响应
     * @throws BusinessException 当站点不存在或更新数据验证失败时抛出
     * @see SiteUpdateDto 更新参数说明
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新站点", description = "更新站点信息")
    @AdminRole()
    public Result<?> updateSite(
            @Parameter(description = "站点ID") @PathVariable @NotNull Long id,
            @Parameter(description = "站点信息") @RequestBody @Valid SiteUpdateDto updateDto,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员更新站点信息，管理员ID：{}，站点ID：{}", adminId, id);
        
        SiteDetailVo updatedSite = siteService.updateSite(id, updateDto, Long.parseLong(adminId));
        return Result.success(updatedSite);
    }

    /**
     * 删除站点接口（仅超级管理员）
     * 
     * <p>此接口用于超级管理员删除不再使用的站点，该操作不可逆，需谨慎操作。</p>
     * 
     * <p>删除前的校验：</p>
     * <ul>
     *   <li>检查站点是否存在关联的闸机设备</li>
     *   <li>检查是否存在未完成的出行记录</li>
     *   <li>确认站点已停止运营和服务</li>
     * </ul>
     * 
     * <p>删除操作将会：</p>
     * <ul>
     *   <li>物理删除站点记录（非软删除）</li>
     *   <li>同步删除相关的缓存数据</li>
     *   <li>记录详细的删除日志和操作记录</li>
     * </ul>
     *
     * @param id 站点ID，必须为正数
     * @param adminId 当前管理员ID，用于记录删除者
     * @return 操作结果响应
     * @throws BusinessException 当站点不存在或存在关联数据时抛出
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除站点", description = "删除指定站点")
    @AdminRole("SUPER_ADMIN")
    public Result<?> deleteSite(
            @Parameter(description = "站点ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员删除站点，管理员ID：{}，站点ID：{}", adminId, id);
        
        siteService.deleteSite(id, Long.parseLong(adminId));
        return Result.success();
    }

    /**
     * 根据关键字搜索站点接口
     * 
     * <p>此接口用于管理员通过关键字快速搜索站点，支持在多个字段中进行模糊匹配。</p>
     * 
     * <p>搜索范围包括：</p>
     * <ul>
     *   <li>站点编码：支持精确匹配和前缀匹配</li>
     *   <li>站点名称：支持中文名称的部分匹配</li>
     *   <li>线路名称：支持线路名称的模糊匹配</li>
     *   <li>地址信息：支持地址关键字搜索</li>
     * </ul>
     * 
     * <p>搜索结果按照相关性排序，精确匹配优先显示，最多返回50条结果。</p>
     *
     * @param keyword 搜索关键字，不能为空，最小长度2个字符
     * @param adminId 当前管理员ID
     * @return 匹配的站点列表
     * @see SiteListVo 站点列表项数据说明
     */
    @GetMapping("/search")
    @Operation(summary = "搜索站点", description = "根据关键字搜索站点")
    @AdminRole()
    public Result<?> searchSites(
            @Parameter(description = "搜索关键字") @RequestParam @NotEmpty String keyword,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员搜索站点，管理员ID：{}，关键字：{}", adminId, keyword);
        
        List<SiteListVo> sites = siteService.searchSitesByKeyword(keyword);
        return Result.success(sites);
    }

    /**
     * 获取所有城市列表接口
     * 
     * <p>此接口用于获取系统中所有已配置站点的城市列表，主要用于下拉框选项和筛选条件。</p>
     * 
     * <p>返回的城市列表特点：</p>
     * <ul>
     *   <li>去重复：同一城市只返回一次</li>
     *   <li>排序：按城市名称拼音字母排序</li>
     *   <li>实时性：仅返回存在有效站点的城市</li>
     * </ul>
     * 
     * <p>该接口通常用于初始化前端的城市选择器和过滤组件。</p>
     *
     * @param adminId 当前管理员ID
     * @return 城市名称列表
     */
    @GetMapping("/cities")
    @Operation(summary = "获取城市列表", description = "获取所有站点的城市列表")
    @AdminRole()
    public Result<?> getAllCities(
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询所有城市列表，管理员ID：{}", adminId);
        
        List<String> cities = siteService.getAllCities();
        return Result.success(cities);
    }

    /**
     * 获取所有线路列表接口
     * 
     * <p>此接口用于获取系统中所有已配置站点的线路名称列表，主要用于下拉框选项和筛选条件。</p>
     * 
     * <p>返回的线路列表特点：</p>
     * <ul>
     *   <li>去重复：同一线路名称只返回一次</li>
     *   <li>排序：按线路编号和名称排序（如1号线、2号线等）</li>
     *   <li>实时性：仅返回存在有效站点的线路</li>
     * </ul>
     * 
     * <p>该接口通常用于初始化前端的线路选择器和站点筛选组件。</p>
     *
     * @param adminId 当前管理员ID
     * @return 线路名称列表
     */
    @GetMapping("/lines")
    @Operation(summary = "获取线路列表", description = "获取所有站点的线路名称列表")
    @AdminRole()
    public Result<?> getAllLineNames(
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员查询所有线路名称列表，管理员ID：{}", adminId);
        
        List<String> lineNames = siteService.getAllLineNames();
        return Result.success(lineNames);
    }
}
package org.software.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Getter;
import lombok.Setter;
import org.software.code.dto.SiteCreateDto;
import org.software.code.dto.SiteSearchDto;
import org.software.code.dto.SiteUpdateDto;
import org.software.code.entity.Site;
import org.software.code.vo.SiteDetailVo;
import org.software.code.vo.SiteListVo;

import java.util.List;

/**
 * SiteService 是站点管理业务服务接口，定义了地铁站点信息的全生命周期管理核心业务方法。
 * 
 * <p>本服务接口封装了站点相关的所有业务逻辑，包括：</p>
 * <ul>
 *   <li>站点信息管理：站点增删改查、详情获取、信息维护</li>
 *   <li>站点搜索功能：关键字搜索、坐标范围查询、多条件筛选</li>
 *   <li>站点状态控制：运营状态管理、维护状态设置、批量操作</li>
 *   <li>基础数据统计：城市列表、线路统计、站点分类统计</li>
 *   <li>数据完整性验证：编码唯一性检查、名称重复验证</li>
 * </ul>
 * 
 * <p>业务特性：</p>
 * <ul>
 *   <li>支持按城市、线路、运营状态的多维度查询</li>
 *   <li>提供基于地理坐标的空间查询能力</li>
 *   <li>实现站点编码和名称的唯一性约束管理</li>
 *   <li>集成站点设备关联管理和状态同步</li>
 *   <li>包含实时统计和数据报表功能</li>
 * </ul>
 * 
 * <p>权限控制：</p>
 * <ul>
 *   <li>ADMIN角色：可查看、编辑站点信息和状态管理</li>
 *   <li>SUPER_ADMIN角色：具有所有权限，包括创建、删除和批量操作</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface SiteService extends IService<Site> {
    /**
     * 分页查询站点列表
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param searchDto  搜索条件
     * @return 站点列表分页结果
     */
    Page<SiteListVo> getSitePage(Integer pageNum, Integer pageSize, SiteSearchDto searchDto);

    /**
     * 根据ID获取站点详细信息
     *
     * @param siteId 站点ID
     * @return 站点详细信息
     */
    SiteDetailVo getSiteDetail(Long siteId);

    /**
     * 创建站点
     *
     * @param createDto 创建站点信息
     * @param adminId   创建管理员ID
     * @return 创建的站点详细信息
     */
    SiteDetailVo createSite(SiteCreateDto createDto, Long adminId);

    /**
     * 更新站点信息
     *
     * @param siteId    站点ID
     * @param updateDto 更新站点信息
     * @param adminId   更新管理员ID
     * @return 更新后的站点详细信息
     */
    SiteDetailVo updateSite(Long siteId, SiteUpdateDto updateDto, Long adminId);

    /**
     * 删除站点（逻辑删除）
     *
     * @param siteId  站点ID
     * @param adminId 删除管理员ID
     * @return 删除结果
     */
    Boolean deleteSite(Long siteId, Long adminId);

    /**
     * 批量删除站点
     *
     * @param siteIds 站点ID列表
     * @param adminId 删除管理员ID
     * @return 删除结果
     */
    Boolean batchDeleteSites(List<Long> siteIds, Long adminId);

    /**
     * 批量更新站点状态
     *
     * @param siteIds 站点ID列表
     * @param status  新状态
     * @param adminId 操作管理员ID
     * @return 更新结果
     */
    Boolean batchUpdateSiteStatus(List<Long> siteIds, String status, Long adminId);

    /**
     * 启用站点
     *
     * @param siteId  站点ID
     * @param adminId 操作管理员ID
     * @return 操作结果
     */
    Boolean enableSite(Long siteId, Long adminId);

    /**
     * 停用站点
     *
     * @param siteId  站点ID
     * @param adminId 操作管理员ID
     * @return 操作结果
     */
    Boolean disableSite(Long siteId, Long adminId);

    /**
     * 设置站点维护状态
     *
     * @param siteId  站点ID
     * @param adminId 操作管理员ID
     * @return 操作结果
     */
    Boolean setMaintenanceStatus(Long siteId, Long adminId);

    /**
     * 根据关键字搜索站点
     *
     * @param keyword 搜索关键字
     * @return 站点列表
     */
    List<SiteListVo> searchSitesByKeyword(String keyword);

    /**
     * 根据坐标范围查询站点
     *
     * @param minLongitude 最小经度
     * @param maxLongitude 最大经度
     * @param minLatitude  最小纬度
     * @param maxLatitude  最大纬度
     * @return 站点列表
     */
    List<SiteListVo> getSitesByCoordinateRange(Double minLongitude, Double maxLongitude,
                                               Double minLatitude, Double maxLatitude);

    /**
     * 获取所有城市列表
     *
     * @return 城市列表
     */
    List<String> getAllCities();

    /**
     * 获取所有线路名称列表
     *
     * @return 线路名称列表
     */
    List<String> getAllLineNames();

    /**
     * 获取站点统计信息
     *
     * @return 站点统计信息
     */
    SiteService.SiteStatisticsVo getSiteStatistics();

    /**
     * 检查站点编码是否存在
     *
     * @param siteCode  站点编码
     * @param excludeId 排除的站点ID
     * @return 是否存在
     */
    Boolean checkSiteCodeExists(String siteCode, Long excludeId);

    /**
     * 检查站点名称是否存在
     *
     * @param siteName  站点名称
     * @param excludeId 排除的站点ID
     * @return 是否存在
     */
    Boolean checkSiteNameExists(String siteName, Long excludeId);

    /**
     * 站点统计视图对象
     */
    @Getter
    @Setter
    class SiteStatisticsVo {
        private Integer totalSites;
        private Integer activeSites;
        private Integer inactiveSites;
        private Integer maintenanceSites;

        private Integer totalCities;

    }
}

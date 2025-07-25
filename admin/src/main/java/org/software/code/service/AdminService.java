package org.software.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.software.code.common.except.BusinessException;
import org.software.code.dto.AdminLoginDto;
import org.software.code.entity.Admin;
import org.software.code.vo.AdminLoginVo;
import org.software.code.vo.AdminProfileVo;

/**
 * AdminService 是管理员业务服务接口，定义了管理员认证、授权和个人信息管理的核心业务方法。
 * 
 * <p>本服务接口封装了管理员相关的所有业务逻辑，包括：</p>
 * <ul>
 *   <li>身份认证：登录验证、密码检查、账户状态验证</li>
 *   <li>JWT管理：令牌生成、刷新和销毁机制</li>
 *   <li>会话管理：登录状态维护、多端登录控制</li>
 *   <li>安全控制：登录失败限制、IP白名单等</li>
 *   <li>个人信息：管理员资料查询和更新</li>
 * </ul>
 * 
 * <p>安全特性：</p>
 * <ul>
 *   <li>密码使用BCrypt加密存储和验证</li>
 *   <li>支持登录失败次数统计和锁定机制</li>
 *   <li>JWT令牌包含有效期和权限信息</li>
 *   <li>支持操作日志记录和安全审计</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface AdminService extends IService<Admin> {
    /**
     * 管理员登录认证方法
     * 
     * <p>此方法处理管理员的登录认证请求，包括用户名和密码的验证，
     * 成功后生成JWT访问令牌和刷新令牌。</p>
     * 
     * <p>认证流程包括：</p>
     * <ol>
     *   <li>验证请求参数的完整性和格式</li>
     *   <li>检查登录失败次数，防止暴力破解</li>
     *   <li>查询管理员账户存在性和状态</li>
     *   <li>验证密码正确性（BCrypt加密）</li>
     *   <li>生成JWT令牌和更新登录信息</li>
     * </ol>
     *
     * @param loginDto 登录请求数据，包含用户名、密码等信息
     * @param clientIp 客户端IP地址，用于安全审计和登录记录
     * @return 登录响应结果，包含JWT令牌和管理员基本信息
     * @throws BusinessException 当认证失败、账户被锁定或其他安全问题时抛出
     */
    AdminLoginVo login(AdminLoginDto loginDto, String clientIp);

    /**
     * 根据管理员ID获取个人信息方法
     * 
     * <p>此方法用于获取指定管理员的详细个人信息，包括基本资料、
     * 角色权限、登录历史等数据。</p>
     * 
     * <p>返回的信息不包含敏感数据如密码等，仅包含可对外展示的公开信息。</p>
     *
     * @param adminId 管理员唯一标识ID
     * @return 管理员个人信息视图对象
     * @throws BusinessException 当管理员不存在时抛出
     */
    AdminProfileVo getProfileById(Long adminId);

      /**
     * 刷新JWT令牌方法
     *
     * <p>此方法用于在访问令牌即将过期时，使用刷新令牌生成新的访问令牌，
     * 延长用户的登录状态，避免频繁重新登录。</p>
     *
     * <p>刷新过程中会重新验证管理员账户状态，确保令牌刷新时账户仍然有效。</p>
     *
     * @param adminId 管理员ID
     * @param clientIp 客户端IP地址，用于安全审计
     * @return 新的JWT令牌和管理员信息
     * @throws BusinessException 当管理员不存在或账户状态异常时抛出
     */
    AdminLoginVo refreshToken(Long adminId, String clientIp);


        /**
     * 管理员登出方法
     *
     * <p>此方法处理管理员的登出请求，清理相关的会话数据和缓存信息。</p>
     *
     * <p>登出操作包括：</p>
     * <ul>
     *   <li>清除Redis中的JWT令牌缓存</li>
     *   <li>清除刷新令牌缓存</li>
     *   <li>记录登出操作日志</li>
     *   <li>更新最后活动时间</li>
     * </ul>
     *
     * <p>此方法设计为幂等操作，多次调用不会产生副作用。</p>
     *
     * @param adminId 管理员ID
     * @param clientIp 客户端IP地址，用于日志记录
     */
    void logout(Long adminId, String clientIp);

}

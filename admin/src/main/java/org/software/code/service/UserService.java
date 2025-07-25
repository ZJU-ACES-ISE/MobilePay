package org.software.code.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.software.code.common.except.BusinessException;
import org.software.code.dto.UserAuditDto;
import org.software.code.dto.UserSearchDto;
import org.software.code.entity.User;
import org.software.code.vo.TravelRecordVo;
import org.software.code.vo.UserDetailVo;
import org.software.code.vo.UserListVo;
import org.software.code.vo.UserStatisticsVo;

import java.util.List;

/**
 * UserService 是用户管理业务服务接口，定义了普通用户的全生命周期管理核心业务方法。
 * 
 * <p>本服务接口封装了用户相关的所有业务逻辑，包括：</p>
 * <ul>
 *   <li>用户信息管理：用户查询、详情获取、信息更新</li>
 *   <li>用户审核流程：注册审核、身份验证、审核记录管理</li>
 *   <li>用户状态控制：启用/禁用账户、状态批量操作</li>
 *   <li>用户行为分析：出行记录追踪、消费统计分析</li>
 *   <li>数据统计功能：用户数量统计、活跃度分析</li>
 * </ul>
 * 
 * <p>业务特性：</p>
 * <ul>
 *   <li>支持多条件复合查询和分页展示</li>
 *   <li>实现完整的用户审核工作流程</li>
 *   <li>提供批量操作能力，提升管理效率</li>
 *   <li>集成出行记录分析，支持用户行为监控</li>
 *   <li>包含实时统计和数据报表功能</li>
 * </ul>
 * 
 * <p>权限控制：</p>
 * <ul>
 *   <li>ADMIN角色：可查看、审核、启用/禁用用户</li>
 *   <li>SUPER_ADMIN角色：具有所有权限，包括批量操作和数据统计</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
public interface UserService extends IService<User> {
    /**
     * 分页查询用户列表方法
     * 
     * <p>此方法提供用户数据的分页查询功能，支持多种搜索条件的组合过滤。</p>
     * 
     * <p>支持的查询条件包括：</p>
     * <ul>
     *   <li>用户状态筛选：PENDING、APPROVED、REJECTED、DISABLED</li>
     *   <li>注册时间范围：支持开始日期和结束日期筛选</li>
     *   <li>关键字搜索：用户名、手机号、真实姓名的模糊匹配</li>
     *   <li>身份认证状态：已认证、未认证用户筛选</li>
     * </ul>
     * 
     * <p>返回结果按注册时间倒序排列，包含用户基本信息但不包含敏感数据。</p>
     *
     * @param pageNum 页码，从1开始
     * @param pageSize 每页记录数，建议范围10-100
     * @param searchDto 搜索条件对象，包含各种筛选参数
     * @return 分页用户列表，包含总数和当前页数据
     * @see UserSearchDto 搜索条件详细说明
     * @see UserListVo 用户列表项数据结构
     */
    Page<UserListVo> getUserPage(Integer pageNum, Integer pageSize, UserSearchDto searchDto);

    /**
     * 根据ID获取用户详细信息方法
     * 
     * <p>此方法用于获取指定用户的完整详细信息，包括基本资料、认证状态、
     * 审核记录等全面数据。</p>
     * 
     * <p>返回的详细信息包括：</p>
     * <ul>
     *   <li>基本信息：用户名、真实姓名、手机号、邮箱</li>
     *   <li>身份信息：身份证号、认证状态、认证时间</li>
     *   <li>账户状态：注册时间、最后登录、当前状态</li>
     *   <li>审核信息：审核记录、审核意见、操作历史</li>
     * </ul>
     * 
     * <p>注意：敏感信息如密码等不会包含在返回结果中。</p>
     *
     * @param userId 用户唯一标识ID，必须为有效的正整数
     * @return 用户详细信息对象，不存在时抛出异常
     * @throws BusinessException 当用户不存在时抛出
     * @see UserDetailVo 用户详情数据结构说明
     */
    UserDetailVo getUserDetail(Long userId);

    /**
     * 审核用户方法（通用审核接口）
     * 
     * <p>此方法提供用户审核的通用接口，可以处理审核通过或拒绝的业务逻辑。</p>
     * 
     * <p>审核流程包括：</p>
     * <ul>
     *   <li>验证用户当前状态是否允许审核</li>
     *   <li>根据审核结果更新用户状态</li>
     *   <li>创建审核记录，记录审核人和审核意见</li>
     *   <li>发送审核结果通知给用户</li>
     *   <li>记录操作日志供后续审计</li>
     * </ul>
     * 
     * <p>审核操作不可逆，建议在重要场景下进行二次确认。</p>
     *
     * @param userId 待审核用户ID
     * @param auditDto 审核信息，包含审核结果和意见
     * @param adminId 执行审核的管理员ID
     * @return 审核操作是否成功
     * @throws BusinessException 当用户状态不允许审核时抛出
     * @see UserAuditDto 审核数据传输对象说明
     */
    Boolean auditUser(Long userId, UserAuditDto auditDto, Long adminId);

    /**
     * 批量更新用户状态方法
     * 
     * <p>此方法提供批量修改多个用户状态的功能，提升管理效率。</p>
     * 
     * <p>支持的状态变更包括：</p>
     * <ul>
     *   <li>APPROVED：批量审核通过</li>
     *   <li>REJECTED：批量审核拒绝</li>
     *   <li>DISABLED：批量禁用账户</li>
     *   <li>ENABLED：批量启用账户</li>
     * </ul>
     * 
     * <p>批量操作具有事务性，要么全部成功，要么全部失败，确保数据一致性。</p>
     * 
     * <p>每个状态变更都会记录操作日志，包含操作人、操作时间和变更详情。</p>
     *
     * @param userIds 用户ID列表，不能为空
     * @param status 目标状态值
     * @param adminId 执行操作的管理员ID
     * @return 批量操作是否全部成功
     * @throws BusinessException 当部分用户状态不允许变更时抛出
     */
    Boolean batchUpdateUserStatus(List<Long> userIds, String status, Long adminId);

    /**
     * 启用用户
     *
     * @param userId  用户ID
     * @param adminId 操作管理员ID
     * @return 操作结果
     */
    Boolean enableUser(Long userId, Long adminId);

    /**
     * 禁用用户
     *
     * @param userId  用户ID
     * @param adminId 操作管理员ID
     * @param reason  禁用原因
     * @return 操作结果
     */
    Boolean disableUser(Long userId, Long adminId, String reason);

    /**
     * 根据关键字搜索用户
     *
     * @param keyword 搜索关键字
     * @return 用户列表
     */
    List<UserListVo> searchUsersByKeyword(String keyword);

    /**
     * 获取待审核用户列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 待审核用户列表
     */
    Page<UserListVo> getPendingUsers(Integer pageNum, Integer pageSize);

    /**
     * 获取用户统计信息
     *
     * @return 用户统计信息
     */
    UserStatisticsVo getUserStatistics();

    /**
     * 审核通过用户
     *
     * @param userId     用户ID
     * @param auditDto   审核信息
     * @param adminId    审核管理员ID
     * @return 审核结果
     */
    Boolean approveUser(Long userId, UserAuditDto auditDto, Long adminId);

    /**
     * 审核拒绝用户
     *
     * @param userId     用户ID
     * @param auditDto   审核信息
     * @param adminId    审核管理员ID
     * @return 审核结果
     */
    Boolean rejectUser(Long userId, UserAuditDto auditDto, Long adminId);

    /**
     * 获取用户出行记录
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 出行记录分页结果
     */
    Page<TravelRecordVo> getUserTravelRecords(Long userId, Integer pageNum, Integer pageSize);

}

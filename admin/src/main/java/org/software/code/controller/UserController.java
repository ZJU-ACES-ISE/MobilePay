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
import org.software.code.dto.UserAuditDto;
import org.software.code.dto.UserSearchDto;
import org.software.code.service.UserService;
import org.software.code.vo.TravelRecordVo;
import org.software.code.vo.UserDetailVo;
import org.software.code.vo.UserListVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * UserController 是用户管理控制器，提供管理员对普通用户进行管理的全部REST API接口。
 * 
 * <p>本控制器负责处理用户的全生命周期管理，包括：</p>
 * <ul>
 *   <li>用户信息查询与搜索：支持分页查询、关键字搜索、多条件筛选</li>
 *   <li>用户审核管理：用户注册审核、审核通过/拒绝、审核记录追踪</li>
 *   <li>用户状态管理：启用/禁用用户账户、状态变更日志</li>
 *   <li>用户行为分析：出行记录查询、消费行为统计</li>
 * </ul>
 * 
 * <p>权限控制说明：</p>
 * <ul>
 *   <li>ADMIN角色：可查看、审核、启用/禁用用户</li>
 *   <li>SUPER_ADMIN角色：具有所有权限，包括永久删除用户数据</li>
 * </ul>
 * 
 * <p>安全特性：</p>
 * <ul>
 *   <li>所有操作均需要管理员身份认证</li>
 *   <li>敏感操作记录审计日志和操作记录</li>
 *   <li>支持数据权限隔离和字段级别权限控制</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Tag(name = "用户管理", description = "用户查询、审核、状态管理等接口")
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 分页查询用户列表接口
     * 
     * <p>此接口用于管理员分页查看系统中所有注册用户的基本信息，支持多种查询条件和排序方式。</p>
     * 
     * <p>支持的查询条件：</p>
     * <ul>
     *   <li>用户状态筛选：PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）、DISABLED（已禁用）</li>
     *   <li>注册时间范围筛选：支持开始和结束日期</li>
     *   <li>关键字搜索：支持手机号、用户名、真实姓名的模糊搜索</li>
     * </ul>
     * 
     * <p>返回数据包含用户的基本信息、注册时间、审核状态等，不包含敏感信息如密码等。</p>
     *
     * @param pageNum 页码，从1开始，默认为1
     * @param pageSize 每页记录数，默认为10，最大不超过100
     * @param searchDto 查询条件，包含状态、时间范围、关键字等筛选条件
     * @param adminId 当前管理员ID，由网关解析JWT后传递
     * @param adminRole 当前管理员角色，用于权限控制
     * @return 分页的用户列表数据
     * @see UserSearchDto 搜索条件说明
     * @see UserListVo 用户列表项数据说明
     */
    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户列表")
    @AdminRole()
    public Result<?> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索条件") @ModelAttribute UserSearchDto searchDto,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询用户列表，管理员ID：{}，页码：{}，页大小：{}，搜索条件：{}", adminId, pageNum, pageSize, searchDto);
        
        Page<UserListVo> userPage = userService.getUserPage(pageNum, pageSize, searchDto);
        return Result.success(userPage);
    }

    /**
     * 获取用户详细信息接口
     * 
     * <p>此接口用于管理员查看指定用户的详细信息，包括基本资料、认证信息、账户状态等。</p>
     * 
     * <p>返回信息包括：</p>
     * <ul>
     *   <li>基本信息：用户名、真实姓名、手机号、邮箱地址</li>
     *   <li>认证信息：身份证号码、认证状态、认证时间</li>
     *   <li>账户状态：注册时间、最后登录时间、账户状态</li>
     *   <li>审核信息：审核状态、审核时间、审核意见</li>
     * </ul>
     * 
     * <p>注意：出于安全考虑，不会返回用户密码等敏感信息。</p>
     *
     * @param id 用户ID，必须为正数
     * @param adminId 当前管理员ID
     * @param adminRole 当前管理员角色
     * @return 用户详细信息
     * @throws BusinessException 当用户不存在时抛出
     * @see UserDetailVo 用户详情数据说明
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    @AdminRole()
    public Result<?> getUserDetail(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询用户详情，管理员ID：{}，用户ID：{}", adminId, id);
        
        UserDetailVo userDetail = userService.getUserDetail(id);
        return Result.success(userDetail);
    }

    /**
     * 根据关键字搜索用户接口
     * 
     * <p>此接口用于管理员通过关键字快速搜索用户，支持在多个字段中进行模糊匹配。</p>
     * 
     * <p>搜索范围包括：</p>
     * <ul>
     *   <li>手机号码：支持部分匹配，如输入1388可匹配13888888888</li>
     *   <li>用户名：支持前后缀匹配，不区分大小写</li>
     *   <li>真实姓名：支持部分匹配，如输入“张”可匹配“张三”</li>
     * </ul>
     * 
     * <p>返回结果按照相关性排序，完全匹配的结果优先显示。</p>
     *
     * @param keyword 搜索关键字，不能为空，最小长度2个字符
     * @param adminId 当前管理员ID
     * @return 匹配的用户列表，最多返回50条结果
     * @see UserListVo 用户列表项数据说明
     */
    @GetMapping("/search")
    @Operation(summary = "搜索用户", description = "根据关键字搜索用户（手机号、用户名、真实姓名）")
    @AdminRole()
    public Result<?> searchUsers(
            @Parameter(description = "搜索关键字") @RequestParam @NotEmpty String keyword,
            @RequestHeader("X-User-Id") String adminId) {
        
        logger.info("管理员搜索用户，管理员ID：{}，关键字：{}", adminId, keyword);
        
        List<UserListVo> users = userService.searchUsersByKeyword(keyword);
        return Result.success(users);
    }

    /**
     * 获取待审核用户列表接口
     * 
     * <p>此接口用于管理员查看所有处于待审核状态的用户，方便进行集中审核处理。</p>
     * 
     * <p>待审核用户包括：</p>
     * <ul>
     *   <li>新注册的用户，尚未通过身份认证</li>
     *   <li>信息变更的用户，需要重新审核</li>
     *   <li>之前被拒绝后重新提交的用户</li>
     * </ul>
     * 
     * <p>返回的用户列表按照提交时间倒序排列，最新提交的审核申请优先显示。</p>
     *
     * @param pageNum 页码，从1开始，默认为1
     * @param pageSize 每页记录数，默认为10
     * @param adminId 当前管理员ID
     * @param adminRole 当前管理员角色
     * @return 分页的待审核用户列表
     * @see UserListVo 用户列表项数据说明
     */
    @GetMapping("/pending")
    @Operation(summary = "获取待审核用户", description = "分页获取待审核的用户列表")
    @AdminRole()
    public Result<?> getPendingUsers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询待审核用户列表，管理员ID：{}，页码：{}，页大小：{}", adminId, pageNum, pageSize);
        
        Page<UserListVo> pendingUsers = userService.getPendingUsers(pageNum, pageSize);
        return Result.success(pendingUsers);
    }

    /**
     * 管理员审核通过用户接口
     * 
     * <p>此接口用于管理员审核通过用户的注册申请或信息变更申请。</p>
     * 
     * <p>审核通过后的操作：</p>
     * <ul>
     *   <li>将用户状态更新为APPROVED（已通过）</li>
     *   <li>创建审核记录，记录审核时间、审核人、审核意见</li>
     *   <li>向用户发送审核通过通知（邮件/短信）</li>
     *   <li>记录操作日志供安全审计</li>
     * </ul>
     * 
     * <p>注意：审核操作不可逆，请谨慎操作。对于重要的身份变更，建议二次确认。</p>
     *
     * @param id 用户ID，必须为正数
     * @param auditDto 审核信息，包含审核意见和备注
     * @param adminId 当前管理员ID，作为审核人记录
     * @param adminRole 当前管理员角色
     * @return 操作结果响应
     * @throws BusinessException 当用户不存在或状态不允许审核时抛出
     * @see UserAuditDto 审核信息说明
     */
    @PostMapping("/{id}/approve")
    @Operation(summary = "审核通过", description = "审核通过用户注册")
    @AdminRole()
    public Result<?> approveUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "审核信息") @RequestBody @Valid UserAuditDto auditDto,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员审核通过用户，管理员ID：{}，用户ID：{}", adminId, id);
        
        userService.approveUser(id, auditDto, Long.parseLong(adminId));
        return Result.success();
    }

    /**
     * 管理员审核拒绝用户接口
     * 
     * <p>此接口用于管理员审核拒绝用户的注册申请或信息变更申请。</p>
     * 
     * <p>审核拒绝后的操作：</p>
     * <ul>
     *   <li>将用户状态更新为REJECTED（已拒绝）</li>
     *   <li>创建审核记录，记录拒绝原因和审核意见</li>
     *   <li>向用户发送审核拒绝通知，包含拒绝原因</li>
     *   <li>用户可根据拒绝意见修改后重新提交审核</li>
     *   <li>记录操作日志供安全审计</li>
     * </ul>
     * 
     * <p>审核拒绝时必须提供明确的拒绝原因，帮助用户理解并改进。</p>
     *
     * @param id 用户ID，必须为正数
     * @param auditDto 审核信息，必须包含拒绝原因
     * @param adminId 当前管理员ID，作为审核人记录
     * @param adminRole 当前管理员角色
     * @return 操作结果响应
     * @throws BusinessException 当用户不存在或状态不允许审核时抛出
     * @see UserAuditDto 审核信息说明
     */
    @PostMapping("/{id}/reject")
    @Operation(summary = "审核拒绝", description = "审核拒绝用户注册")
    @AdminRole()
    public Result<?> rejectUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "审核信息") @RequestBody @Valid UserAuditDto auditDto,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员审核拒绝用户，管理员ID：{}，用户ID：{}", adminId, id);
        
        userService.rejectUser(id, auditDto, Long.parseLong(adminId));
        return Result.success();
    }

    /**
     * 启用用户账户接口
     * 
     * <p>此接口用于管理员启用被禁用的用户账户，恢复用户的正常使用权限。</p>
     * 
     * <p>启用账户后的效果：</p>
     * <ul>
     *   <li>用户可以正常登录系统</li>
     *   <li>恢复所有业务功能的使用权限</li>
     *   <li>恢复支付、转账等资金操作功能</li>
     *   <li>可以接收系统通知和推送消息</li>
     * </ul>
     * 
     * <p>注意：仅ADMIN及SUPER_ADMIN角色可以执行此操作，操作将被记录在审计日志中。</p>
     *
     * @param id 用户ID，必须为正数
     * @param adminId 当前管理员ID，用于操作日志记录
     * @param adminRole 当前管理员角色，用于权限验证
     * @return 操作结果响应
     * @throws BusinessException 当用户不存在或已经处于激活状态时抛出
     */
    @PostMapping("/{id}/enable")
    @Operation(summary = "启用用户", description = "启用被禁用的用户账号")
    @AdminRole("ADMIN")
    public Result<?> enableUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员启用用户，管理员ID：{}，用户ID：{}", adminId, id);
        
        userService.enableUser(id, Long.parseLong(adminId));
        return Result.success();
    }

    /**
     * 禁用用户账户接口
     * 
     * <p>此接口用于管理员禁用用户账户，限制用户使用系统功能。通常用于处理违规行为或安全问题。</p>
     * 
     * <p>禁用账户后的限制：</p>
     * <ul>
     *   <li>用户无法登录系统</li>
     *   <li>所有业务功能被暂停</li>
     *   <li>禁止进行支付、转账、提现等资金操作</li>
     *   <li>停止发送系统通知和营销消息</li>
     * </ul>
     * 
     * <p>禁用原因将被记录在系统中，用于后续的审计和用户投诉处理。</p>
     *
     * @param id 用户ID，必须为正数
     * @param reason 禁用原因，可选，用于记录和对用户说明
     * @param adminId 当前管理员ID，用于操作日志记录
     * @param adminRole 当前管理员角色，用于权限验证
     * @return 操作结果响应
     * @throws BusinessException 当用户不存在或已经处于禁用状态时抛出
     */
    @PostMapping("/{id}/disable")
    @Operation(summary = "禁用用户", description = "禁用用户账号")
    @AdminRole("ADMIN")
    public Result<?> disableUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "禁用原因") @RequestParam(required = false) String reason,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员禁用用户，管理员ID：{}，用户ID：{}，原因：{}", adminId, id, reason);
        
        userService.disableUser(id, Long.parseLong(adminId), reason);
        return Result.success();
    }

    /**
     * 获取用户出行记录接口
     * 
     * <p>此接口用于管理员查看指定用户的出行记录，包括地铁、公交等交通工具的使用记录。</p>
     * 
     * <p>返回信息包括：</p>
     * <ul>
     *   <li>出行时间：上车和下车的具体时间</li>
     *   <li>出行路线：起始站点和目的地站点</li>
     *   <li>费用信息：出行费用和优惠情况</li>
     *   <li>支付方式：余额支付、银行卡支付等</li>
     *   <li>设备信息：使用的闸机设备编号</li>
     * </ul>
     * 
     * <p>记录按时间倒序排列，最近的出行记录优先显示。</p>
     *
     * @param id 用户ID，必须为正数
     * @param pageNum 页码，从1开始，默认为1
     * @param pageSize 每页记录数，默认为10
     * @param adminId 当前管理员ID
     * @param adminRole 当前管理员角色
     * @return 分页的出行记录数据
     * @throws BusinessException 当用户不存在时抛出
     * @see TravelRecordVo 出行记录数据说明
     */
    @GetMapping("/{id}/travel-records")
    @Operation(summary = "查看用户出行记录", description = "查看指定用户的出行记录")
    @AdminRole()
    public Result<?> getUserTravelRecords(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestHeader("X-User-Id") String adminId,
            @RequestHeader("X-User-Role") String adminRole) {
        
        logger.info("管理员查询用户出行记录，管理员ID：{}，用户ID：{}，页码：{}，页大小：{}", adminId, id, pageNum, pageSize);
        
        Page<TravelRecordVo> travelRecords = userService.getUserTravelRecords(id, pageNum, pageSize);
        return Result.success(travelRecords);
    }
}
package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Admin 管理员实体类，对应数据库中的admin表，用于存储系统管理员的基本信息和认证数据。
 * 
 * <p>该实体类定义了管理员的核心属性，包括：</p>
 * <ul>
 *   <li>身份标识：用户名、密码、角色等认证信息</li>
 *   <li>联系方式：邮箱、手机号等通信信息</li>
 *   <li>个人信息：真实姓名等基本资料</li>
 *   <li>状态管理：账户状态、登录记录等运营数据</li>
 *   <li>时间追踪：创建时间、更新时间等审计信息</li>
 * </ul>
 * 
 * <p>业务特性：</p>
 * <ul>
 *   <li>支持多角色管理：ADMIN（普通管理员）、SUPER_ADMIN（超级管理员）</li>
 *   <li>账户状态控制：ACTIVE（活跃）、INACTIVE（禁用）、LOCKED（锁定）</li>
 *   <li>登录追踪：记录最后登录时间和IP地址，用于安全审计</li>
 *   <li>密码安全：使用BCrypt加密存储，不可明文查看</li>
 * </ul>
 * 
 * <p>数据库映射：</p>
 * <ul>
 *   <li>表名：admin</li>
 *   <li>主键：id（自增长）</li>
 *   <li>唯一约束：username（用户名唯一）</li>
 *   <li>索引：email, phone（便于查询和登录）</li>
 * </ul>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员唯一标识ID
     * 
     * <p>数据库主键，自增长类型，系统自动生成，作为管理员的唯一标识。</p>
     * 
     * <p>用途：</p>
     * <ul>
     *   <li>作为JWT token中的用户标识</li>
     *   <li>用于关联操作日志和审计记录</li>
     *   <li>管理员权限验证的依据</li>
     * </ul>
     */

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;

    @TableField("status")
    private String status;

    @TableField("last_login_time")
    private Date lastLoginTime;

    @TableField("last_login_ip")
    private String lastLoginIp;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;


    public boolean isActive() {
        return  status != null && status.equals("ACTIVE");
    }
}

package org.software.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author "101"计划《软件工程》实践教材案例团队
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("phone")
    private String phone;

    @TableField("username")
    private String username;

    @TableField("real_name")
    private String realName;

    @TableField("id_card")
    private String idCard;

    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    @TableField("payment_password")
    private String paymentPassword;

    @TableField("avatar")
    private String avatar;

    @TableField("status")
    private String status;

    @TableField("balance")
    private BigDecimal balance;

    @TableField("created_time")
    private Date createdTime;

    @TableField("updated_time")
    private Date updatedTime;
}
